package cn.edu.nju.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: TagVO
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class TagVO {
    String tag;
    String message;
    boolean isRelease;
    String taggerName;
    String taggerEmail;
    String taggerDate;
}
