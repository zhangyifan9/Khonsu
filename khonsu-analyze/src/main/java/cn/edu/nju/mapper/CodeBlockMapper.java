package cn.edu.nju.mapper;

import cn.edu.nju.domain.CodeBlockAggregation;
import cn.edu.nju.domain.entity.CodeBlock;
import cn.edu.nju.domain.vo.CoreCodeBlockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: CodeBlockMapper
 * @Description: TODO
 * @Author panpan
 */
@Mapper
public interface CodeBlockMapper {
    void insertCodeBlock(CodeBlock codeBlock);

    void batchInsertCodeBlock(List<CodeBlock> codeBlocks);

    void deleteCodeBlockByRepoName(String repoName);

    List<CodeBlockAggregation> getCodeBlockAggregationList(String repoName);

    List<CoreCodeBlockVO> getCoreCodeBlockList(@Param("repoName") String repoName, @Param("tag") String tag);
}
