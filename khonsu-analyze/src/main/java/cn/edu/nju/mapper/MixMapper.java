package cn.edu.nju.mapper;

import cn.edu.nju.domain.vo.CodeBlockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: MixMapper
 * @Description: TODO
 * @Author panpan
 */
@Mapper
public interface MixMapper {
    // xml和方法名都会爆红，但运行没问题
    //List<List<?>> selectCodeBlockVOByReleaseId(@Param("releaseId") Long releaseId, @Param("offset") int offset, @Param("limit") int limit);

    List<CodeBlockVO> selectCodeBlockVOByReleaseId(@Param("releaseId") Long releaseId);

    List<CodeBlockVO> selectCodeBlockVOAvg(@Param("repoName") String repoName, @Param("tag") String tag);
}
