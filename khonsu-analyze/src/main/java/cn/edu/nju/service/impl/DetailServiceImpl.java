package cn.edu.nju.service.impl;

import cn.edu.nju.domain.CodeBlockAggregation;
import cn.edu.nju.domain.TagsWithContent;
import cn.edu.nju.domain.entity.Release;
import cn.edu.nju.domain.vo.CodeBlockAggregationVO;
import cn.edu.nju.domain.vo.CodeBlockVO;
import cn.edu.nju.domain.vo.CoreCodeBlockVO;
import cn.edu.nju.mapper.*;
import cn.edu.nju.service.DetailService;
import cn.edu.nju.util.GitHubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName: DetailServiceImpl
 * @Description: TODO
 * @Author panpan
 */
@Service
public class DetailServiceImpl implements DetailService {

    @Value("${nju.token}")
    private String token;
    @Autowired
    ReleaseMapper releaseMapper;

    @Autowired
    CodeBlockMapper codeBlockMapper;
    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    RCLinkMapper rcLinkMapper;

    @Autowired
    MixMapper mixMapper;

    @Override
    public List<String> selectAllRepoName() {
        return projectMapper.selectAllRepoName();
    }

    @Override
    public void updateReleaseState(Long id, int state) {
        releaseMapper.updateReleaseStateById(state, id);
    }

    @Override
    public String updateScore(Long releaseId, Long codeBlockId, String score) {
        rcLinkMapper.updateScore(releaseId, codeBlockId, score);
        if (score.equals("1.00000")) return "1";
        return "0";
    }


    @Override
    public List<Release> selectReleaseByRepoName(String repoName) {
        return releaseMapper.selectReleaseByRepoName(repoName);
    }

    // @Override
    // public CodeBlockPage selectCodeBlockVOByReleaseId(Long releaseId, int offset, int limit) {
    //     List<List<?>> lists = mixMapper.selectCodeBlockVOByReleaseId(releaseId, offset, limit);
    //     List<CodeBlockVO> codeBlockVOList = (List<CodeBlockVO>) lists.get(0);
    //     int count = (Integer) lists.get(1).get(0);
    //     try {
    //         for (CodeBlockVO codeBlockVO : codeBlockVOList) {
    //             codeBlockVO.setTags(codeBlockVO.getTags().replace("&", " | "));
    //             codeBlockVO.setContent(new String(Files.readAllBytes(Paths.get(codeBlockVO.getContent()))));
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return new CodeBlockPage(codeBlockVOList, count);
    // }

    public List<CodeBlockVO> selectCodeBlockVOByReleaseId(Long releaseId) {
        List<CodeBlockVO> codeBlockVOList = mixMapper.selectCodeBlockVOByReleaseId(releaseId);;
        try {
            for (CodeBlockVO codeBlockVO : codeBlockVOList) {
                codeBlockVO.setTags(codeBlockVO.getTags().replace("&", " | "));
                codeBlockVO.setContent(new String(Files.readAllBytes(Paths.get(codeBlockVO.getContent()))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codeBlockVOList;
    }

    @Override
    public List<CodeBlockVO> selectCodeBlockVOTopOrBot(String repoName, String tag, boolean isTop, int count) {
        List<CodeBlockVO> allCB = mixMapper.selectCodeBlockVOAvg(repoName, tag);
        if (!isTop) {
            Collections.reverse(allCB);
        }

        List<CodeBlockVO> res = allCB.subList(0, Math.min(allCB.size(), count));
        try {
            for (CodeBlockVO codeBlockVO : res) {
                codeBlockVO.setTags(codeBlockVO.getTags().replace("&", " | "));
                codeBlockVO.setContent(new String(Files.readAllBytes(Paths.get(codeBlockVO.getContent()))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public String getBaseTag(String userName, String repoName, String lastTag) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(GitHubUtil.getBaseTag(userName, repoName, lastTag, token));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    @Override
    public List<CodeBlockAggregationVO> getCodeBlockAggregation(String repoName) {
        Jedis jedis = new Jedis("localhost", 6379);
        List<CodeBlockAggregationVO> res = new ArrayList<>();
        List<CodeBlockAggregation> codeBlockAggregations = codeBlockMapper.getCodeBlockAggregationList(repoName);
        try {
            for (CodeBlockAggregation cba : codeBlockAggregations) {
                CodeBlockAggregationVO cbav = new CodeBlockAggregationVO();
                cbav.setClassPath(cba.getClassPath().replaceAll("\\\\", "/"));
                String[] tagsList = cba.getTagsList().split("=");
                String[] filePathList = cba.getFilePathList().split("=");
                List<TagsWithContent> codes = new ArrayList<>();
                for (int i = 0; i < cba.getCount(); i++) {
                    String[] tags = tagsList[i].split("&");
                    StringBuilder sb = new StringBuilder();
                    // 判断什么版本新增，什么版本删除
                    if (!tags[tags.length-1].trim().equals("base")) {
                        sb.append("于" + tags[tags.length-1] + "新增");
                    } else {
                        sb.append("base版本就存在");
                    }
                    sb.append("，");
                    if (Integer.parseInt(jedis.hget(repoName, tags[0].trim())) - 1 > 0) {
                        sb.append("于" + jedis.hget(repoName, String.valueOf(Integer.parseInt(jedis.hget(repoName, tags[0].trim())) - 1)) + "删除");
                    } else {
                        sb.append("最新版本还存在");
                    }
                    codes.add(new TagsWithContent(tagsList[i], new String(Files.readAllBytes(Paths.get(filePathList[i]))), sb.toString()));
                }
                Collections.sort(codes, Comparator.comparing(TagsWithContent::getTags).reversed());
                cbav.setCodeBlocks(codes);
                res.add(cbav);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<CoreCodeBlockVO> getCoreCodeBlock(String repoName, String tag) {
        List<CoreCodeBlockVO> res = codeBlockMapper.getCoreCodeBlockList(repoName, tag);
        try {
            for (CoreCodeBlockVO coreCodeBlockVO : res) {
                coreCodeBlockVO.setTags(coreCodeBlockVO.getTags().replace("&", " | "));
                coreCodeBlockVO.setContent(new String(Files.readAllBytes(Paths.get(coreCodeBlockVO.getContent()))));
            }
            Collections.sort(res, Comparator.comparing(CoreCodeBlockVO::getTags));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String getCodeFile(String userName, String repoName, String tag, String path) {
        Jedis jedis = new Jedis("localhost", 6379);
        String hash = jedis.hget(repoName + "-hash", tag);
        path = path.replaceAll("\\\\", "/");
        return "https://github.com/" + userName + "/" + repoName + "/blob/" + hash + "/" + path + "||" + "https://github.com/" + userName + "/" + repoName + "/commits/" + hash + "/" + path;
    }
}
