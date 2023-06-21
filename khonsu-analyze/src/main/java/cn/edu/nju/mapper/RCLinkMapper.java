package cn.edu.nju.mapper;

import cn.edu.nju.domain.entity.RCLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: RCLinkMapper
 * @Description: TODO
 * @Author panpan
 */
@Mapper
public interface RCLinkMapper {
    void insertRCLink(RCLink rcLink);

    void batchInsertRCLink(List<RCLink> rcLinks);

    void updateScore(@Param("releaseId") Long releaseId, @Param("codeBlockId") Long codeBlockId, @Param("score") String score);

    void deleteRCLinkByRepoName(String repoName);
}
