package cn.edu.nju.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: TagWithDate
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
public class TagWithDateVO {
    String tag;
    String date;

    @Override
    public String toString() {
        return "[tag:" + tag + ", date:" + date + "]";
    }
}
