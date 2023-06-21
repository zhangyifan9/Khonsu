package cn.edu.nju.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: CodeBlockAggregation
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class CodeBlockAggregation {
    String classPath;
    String tagsList;
    String filePathList;
    int count;
}
