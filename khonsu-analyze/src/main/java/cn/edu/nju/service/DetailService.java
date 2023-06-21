package cn.edu.nju.service;

import cn.edu.nju.domain.entity.Release;
import cn.edu.nju.domain.vo.CodeBlockAggregationVO;
import cn.edu.nju.domain.vo.CodeBlockVO;
import cn.edu.nju.domain.vo.CoreCodeBlockVO;

import java.util.List;

/**
 * @InterfaceName: DetailService
 * @Description: TODO
 * @Author panpan
 */
public interface DetailService {
    List<String> selectAllRepoName();

    void updateReleaseState(Long id, int state);

    String updateScore(Long releaseId, Long codeBlockId, String score);

    List<Release> selectReleaseByRepoName(String repoName);

    // CodeBlockPage selectCodeBlockVOByReleaseId(Long releaseId, int offset, int limit);

    List<CodeBlockVO> selectCodeBlockVOByReleaseId(Long releaseId);

    List<CodeBlockVO> selectCodeBlockVOTopOrBot(String repoName, String tag, boolean isTop, int count);

    String getBaseTag(String userName, String repoName, String lastTag);

    List<CodeBlockAggregationVO> getCodeBlockAggregation(String repoName);

    List<CoreCodeBlockVO> getCoreCodeBlock(String repoName, String tag);

    String getCodeFile(String userName, String repoName, String tag, String path);
}
