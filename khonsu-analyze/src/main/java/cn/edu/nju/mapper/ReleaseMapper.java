package cn.edu.nju.mapper;

import cn.edu.nju.domain.entity.Release;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: ReleaseMapper
 * @Description: TODO
 * @Author panpan
 */
@Mapper
public interface ReleaseMapper {
    void insertRelease(Release release);

    void batchInsertRelease(List<Release> release);

    List<Release> selectReleaseByRepoName(String repoName);

    void updateReleaseStateById(@Param("readState") Integer readState, @Param("id") Long id);

    void deleteReleaseByRepoName(String repoName);
}
