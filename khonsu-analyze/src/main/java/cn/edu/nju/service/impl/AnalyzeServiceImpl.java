package cn.edu.nju.service.impl;

import at.jku.isse.ecco.api.resource.ArtifactsResource;
import cn.edu.nju.domain.entity.CodeBlock;
import cn.edu.nju.domain.entity.RCLink;
import cn.edu.nju.domain.entity.Release;
import cn.edu.nju.irtool.document.ArtifactsCollection;
import cn.edu.nju.irtool.document.TextDataset;
import cn.edu.nju.irtool.ir.IR;
import cn.edu.nju.irtool.preprocess.DataPreprecess;
import cn.edu.nju.mapper.CodeBlockMapper;
import cn.edu.nju.mapper.RCLinkMapper;
import cn.edu.nju.mapper.ReleaseMapper;
import cn.edu.nju.service.AnalyzeService;
import cn.edu.nju.util.GitHubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: AnalyzeService
 * @Description: TODO
 * @Author panpan
 */
@Service
public class AnalyzeServiceImpl implements AnalyzeService {
    @Value("${nju.token}")
    private String token;

    @Value("${nju.path}")
    private String path;

    @Autowired
    private ReleaseMapper releaseMapper;

    @Autowired
    private CodeBlockMapper codeBlockMapper;

    @Autowired
    private RCLinkMapper rcLinkMapper;

    /**
     * 切分传进来的 String 为一条一条的 release
     * 切分源码为一个个 codeBlock
     * Java 源码在 path/java/repoName
     * ecco 的工作路径在 path/repo
     */
    @Override
    public void preprocess(String allRNs, String repoName) {
        // release 处理
        splitRNs(allRNs, repoName);

        // ecco处理源码
        ArtifactsResource ar = new ArtifactsResource(path + "\\repo");
        ar.commitAllVersions(repoName, path + "\\java");
        try {
            ar.compose(repoName, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ar.close();
    }

    /**
     * 分割传进来的 rn 文本，并保存到指定路径
     */
    @Override
    public void splitRNs(String allRNs, String repoName) {
        GitHubUtil.splitRNs(allRNs, repoName, path);
    }

    /**
     * 主体逻辑只负责分析并记录
     * 处理后的源码在 path/code/name
     * 处理后的 release 在 path/release/name
     */
    @Override
    public void analyzeProject(String allRNs, String repoName) {
        preprocess(allRNs, repoName);

        // release 和 code 在 IR 前的预处理
        DataPreprecess dataPreprecess = new DataPreprecess(repoName, path);
        dataPreprecess.arrangeData();
        TextDataset textDataset = new TextDataset(dataPreprecess.getUcPreProcessedPath(), dataPreprecess.getClassProcessedPath());

        Map<String, Release> sourceMap = new LinkedHashMap<>();
        Map<String, CodeBlock> targetMap = new LinkedHashMap<>();

        deleteByRepoName(repoName);
        saveRlsAndCode(textDataset, repoName, sourceMap, targetMap);
        computeAndSaveSimilarity(textDataset, repoName, sourceMap, targetMap);
    }

    /**
     * TODO (如果之后支持需要改)
     * 目前不支持一个 repoName 对应多个创建的项目
     * 每次创建新项目时，会删除 repoName 相同的数据库表条目
     */
    @Override
    public void deleteByRepoName(String repoName) {
        rcLinkMapper.deleteRCLinkByRepoName(repoName);
        releaseMapper.deleteReleaseByRepoName(repoName);
        codeBlockMapper.deleteCodeBlockByRepoName(repoName);
    }


    /**
     * 数据集预处理以后，将 release 和 codeBlock 进行入库处理
     * 以便之后通过 id 记录相似度
     */
    @Override
    public void saveRlsAndCode(TextDataset textDataset, String repoName, Map<String, Release> releaseMap, Map<String, CodeBlock> codeBlockMap) {
        Jedis jedis = new Jedis("localhost", 6379);
        Map<String, String> tagsMap = new HashMap<>();

        ArtifactsCollection sourceCollection = textDataset.getSourceCollection();
        ArtifactsCollection targetCollection = textDataset.getTargetCollection();

        sourceCollection.keySet().forEach(source -> {
            // source格式: rlsTag&&cat&&编号
            String[] rlsTag_cat = source.split("&&");
            String rlsTag = jedis.hget(repoName, rlsTag_cat[0]);
            String cat = rlsTag_cat[1];
            StringBuilder releaseFilePath = new StringBuilder()
                    .append(path + "\\release\\" + repoName)
                    .append("\\")
                    .append(source.replaceAll("&&", "\\\\"))
                    .append(".txt");
            StringBuilder rn = new StringBuilder();
            try {
                rn.append(new String(Files.readAllBytes(Paths.get(releaseFilePath.toString()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            releaseMap.put(source, new Release(0L, repoName, rlsTag, cat, rn.toString(), 0));
        });

        targetCollection.keySet().forEach(target -> {
            // target格式: cbTags&&cbPath
            String[] cbTags_cbPath = target.split("&&");
            String cbTags = cbTags_cbPath[0];
            String cbPath = cbTags_cbPath[1];
            StringBuilder tags = new StringBuilder();
            if (tagsMap.containsKey(cbTags)) {
                tags.append(tagsMap.get(cbTags));
            } else {
                String newCbTags = Arrays.stream(cbTags.split("&"))
                        .map(str -> jedis.hget(repoName, str))
                        .collect(Collectors.joining("&"));
                tags.append(newCbTags);
                tagsMap.put(cbTags, newCbTags);
            }
            StringBuilder cbFilePath = new StringBuilder()
                    .append(path + "\\code\\" + repoName)
                    .append("\\")
                    .append(target.replaceAll("&&", "\\\\"))
                    .append(".txt");
            codeBlockMap.put(target, new CodeBlock(0L, repoName, tags.toString(), cbPath.replace("&", "\\"), cbFilePath.toString()));
        });

        // // TODO release 批量插入× 目前是一条一条插入，这样能获取自增 ID
        // releaseMap.values().forEach(release -> {
        //     releaseMapper.insertRelease(release);
        // });
        //
        // // TODO codeBlock 批量插入× 同上
        // codeBlockMap.values().forEach(codeBlock -> {
        //     codeBlockMapper.insertCodeBlock(codeBlock);
        // });

        List<Release> releases = releaseMap.values().stream().collect(Collectors.toList());
        int batchSize1 = 100;
        for (int i = 0; i < releases.size(); i += batchSize1) {
            int toIndex = Math.min(i + batchSize1, releases.size());
            releaseMapper.batchInsertRelease(releases.subList(i, toIndex));
        }

        List<CodeBlock> codeBlocks = codeBlockMap.values().stream().collect(Collectors.toList());
        int batchSize2 = 1000;
        for (int i = 0; i < codeBlocks.size(); i += batchSize2) {
            int toIndex = Math.min(i + batchSize2, codeBlocks.size());
            codeBlockMapper.batchInsertCodeBlock(codeBlocks.subList(i, toIndex));
        }

        jedis.close();
    }


    @Override
    public void computeAndSaveSimilarity(TextDataset textDataset, String repoName, Map<String, Release> sourceMap, Map<String, CodeBlock> targetMap) {
        String irModelName = "cn.edu.nju.irtool.ir.VSM";
        List<RCLink> rcLinks = IR.compute(textDataset, irModelName, repoName, sourceMap, targetMap);
        saveSimilarity(rcLinks);
    }

    /**
     * 批量插入相似度计算结果
     */
    @Override
    public void saveSimilarity(List<RCLink> rcLinks) {
        int batchSize = 1000;
        for (int i = 0; i < rcLinks.size(); i += batchSize) {
            int toIndex = Math.min(i + batchSize, rcLinks.size());
            rcLinkMapper.batchInsertRCLink(rcLinks.subList(i, toIndex));
        }
    }

    @Override
    public void test() {
        String repoName = "spring-framework";
        DataPreprecess dataPreprecess = new DataPreprecess(repoName, path);
        dataPreprecess.arrangeData();
        TextDataset textDataset = new TextDataset(dataPreprecess.getUcPreProcessedPath(), dataPreprecess.getClassProcessedPath());
        Map<String, Release> sourceMap = new LinkedHashMap<>();
        Map<String, CodeBlock> targetMap = new LinkedHashMap<>();

        deleteByRepoName(repoName);
        saveRlsAndCode(textDataset, repoName, sourceMap, targetMap);
        computeAndSaveSimilarity(textDataset, repoName, sourceMap, targetMap);
    }
}
