package cn.edu.nju.domain.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: GHRN
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class GHRN {
    String tag;
    List<GHRNCategory> categories;
}
