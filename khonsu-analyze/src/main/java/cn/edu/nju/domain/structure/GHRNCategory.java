package cn.edu.nju.domain.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: GHRNCategory
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class GHRNCategory {
    String category;
    List<String> rns;
}
