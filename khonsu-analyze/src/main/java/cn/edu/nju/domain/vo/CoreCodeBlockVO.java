package cn.edu.nju.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: CoreCodeBlockVO
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class CoreCodeBlockVO {
    String tags;
    String classPath;
    String content;
}
