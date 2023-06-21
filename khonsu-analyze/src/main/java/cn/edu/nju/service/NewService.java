package cn.edu.nju.service;

import cn.edu.nju.domain.vo.TagVO;
import cn.edu.nju.domain.vo.TagWithDateVO;

import java.util.List;

/**
 * @InterfaceName: ImportService
 * @Description: TODO
 * @Author panpan
 */
public interface NewService {
    void createProject(String userName, String repoName);

    void deleteProject(String userName, String repoName);

    List<TagWithDateVO> getTagWithDateList(String userName, String repoName);

    List<TagVO> getTagVOList(String userName, String repoName, String tag1, String tag2);

    boolean downloadSpecificVersions(String userName, String repoName, List<String> tags);

    List<String> getRNs(String userName, String repoName, List<String> tags);

}
