package cn.edu.nju.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @ClassName: Release
 * @Description: release实体类
 * @Author panpan
 */
@Data
@AllArgsConstructor
@Getter
public class Release {
    Long id;
    String repoName;
    String tag;
    String category;
    String content;
    Integer readState;
}
