package cn.edu.nju.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @ClassName: CodeBlock
 * @Description: codeBlock实体类
 * @Author panpan
 */
@Data
@AllArgsConstructor
@Getter
public class CodeBlock {
    Long id;
    String repoName;
    String tags;
    String classPath;
    String filePath;
}
