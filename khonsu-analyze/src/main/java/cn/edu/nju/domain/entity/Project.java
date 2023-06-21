package cn.edu.nju.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName: Project
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class Project {
    Long id;
    String userName;
    String repoName;
    Timestamp updateTime;
}
