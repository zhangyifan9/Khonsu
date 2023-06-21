package cn.edu.nju.domain.vo;

import cn.edu.nju.domain.TagsWithContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: CodeBlockAggregationVO
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeBlockAggregationVO {
    String classPath;
    List<TagsWithContent> codeBlocks;
}
