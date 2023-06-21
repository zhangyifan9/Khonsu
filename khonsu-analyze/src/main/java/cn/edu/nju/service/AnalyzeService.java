package cn.edu.nju.service;

import cn.edu.nju.domain.entity.CodeBlock;
import cn.edu.nju.domain.entity.RCLink;
import cn.edu.nju.domain.entity.Release;
import cn.edu.nju.irtool.document.TextDataset;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName: AnalyzeService
 * @Description: TODO
 * @Author panpan
 */
public interface AnalyzeService {

    void preprocess(String allRNs, String repoName);

    void splitRNs(String allRNs, String repoName);

    void analyzeProject(String allRNs, String repoName);

    void deleteByRepoName(String repoName);

    void saveRlsAndCode(TextDataset textDataset, String repoName, Map<String, Release> releaseMap, Map<String, CodeBlock> codeBlockMap);

    void computeAndSaveSimilarity(TextDataset textDataset, String repoName, Map<String, Release> sourceMap, Map<String, CodeBlock> targetMap);

    void saveSimilarity(List<RCLink> rcLinks);

    void test();
}
