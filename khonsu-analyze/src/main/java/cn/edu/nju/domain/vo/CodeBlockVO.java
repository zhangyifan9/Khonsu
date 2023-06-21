package cn.edu.nju.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: CodeBlockVO
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class CodeBlockVO {
    Long codeBlockId;
    String classPath;
    String tags;
    String score;
    String content;
    Boolean isChange;
}
