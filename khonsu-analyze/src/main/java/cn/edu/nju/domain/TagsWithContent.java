package cn.edu.nju.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: TagsWithContent
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class TagsWithContent {
    String tags;
    String content;
    String note;
}
