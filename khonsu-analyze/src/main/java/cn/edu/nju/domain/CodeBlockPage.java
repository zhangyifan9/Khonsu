package cn.edu.nju.domain;

import cn.edu.nju.domain.vo.CodeBlockVO;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: CodeBlockPage
 * @Description: TODO
 * @Author panpan
 */
@Data
public class CodeBlockPage {
    List<CodeBlockVO> codeBlockVOList;
    int total;

    public CodeBlockPage(List<CodeBlockVO> codeBlockVOList, int total) {
        this.codeBlockVOList = codeBlockVOList;
        this.total = total;
    }
}
