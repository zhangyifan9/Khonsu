package cn.edu.nju.mapper;

import cn.edu.nju.domain.vo.ProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: ProjectMapper
 * @Description: TODO
 * @Author panpan
 */
@Mapper
public interface ProjectMapper {
    void insertProject(@Param("userName") String userName, @Param("repoName") String repoName);

    List<String> selectAllRepoName();

    List<ProjectVO> selectAllProject();

    void deleteProjectByRepoName(String repoName);

    void updateProjectById(Long id);
}
