package cn.edu.nju.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: ProjectVO
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class ProjectVO {
    Long id;
    String owner;
    String name;
    Date date;
}
