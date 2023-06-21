package cn.edu.nju.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: AnalyzeRequest
 * @Description: TODO
 * @Author panpan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeRequest {
    String userName;
    String repoName;
    String tags;
    String rns;
}
