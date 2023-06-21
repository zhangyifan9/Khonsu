package cn.edu.nju.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: RCLink
 * @Description: release与codeBlock关联实体类
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class RCLink {
    Long id;
    String repoName;
    Long releaseId;
    Long codeBlockId;
    String score;
    Boolean isChange;
}
