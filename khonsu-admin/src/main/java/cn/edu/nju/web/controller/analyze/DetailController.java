package cn.edu.nju.web.controller.analyze;

import cn.edu.nju.common.core.controller.BaseController;
import cn.edu.nju.common.core.domain.AjaxResult;
import cn.edu.nju.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DetailController
 * @Description: TODO
 * @Author panpan
 */
@RestController
@RequestMapping("/analyze/detail")
public class DetailController extends BaseController {
    @Autowired
    private DetailService detailService;

    @GetMapping(value = "/project")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getProject() {
        return AjaxResult.success(detailService.selectAllRepoName());
    }

    @GetMapping(value = "/release")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getRelease(@RequestParam("repoName") String repoName) {
        return AjaxResult.success(detailService.selectReleaseByRepoName(repoName));
    }

    // @GetMapping(value = "/code")
    // @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    // public AjaxResult getCodeBlock(@RequestParam("releaseId") Long releaseId, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
    //     return AjaxResult.success(detailService.selectCodeBlockVOByReleaseId(releaseId, (pageNum - 1) * pageSize, pageSize));
    // }

    @GetMapping(value = "/code")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getCodeBlock(@RequestParam("releaseId") Long releaseId) {
        return AjaxResult.success(detailService.selectCodeBlockVOByReleaseId(releaseId));
    }

    @GetMapping(value = "/state")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult updateReleaseState(@RequestParam("releaseId") Long releaseId, @RequestParam("state") int state) {
        detailService.updateReleaseState(releaseId, state);
        return AjaxResult.success("更新成功");
    }

    @GetMapping(value = "/score")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult updateScore(@RequestParam("releaseId") Long releaseId, @RequestParam("codeBlockId") Long codeBlockId, @RequestParam("score") String score) {
        String res = detailService.updateScore(releaseId, codeBlockId, score);
        return AjaxResult.success("score更新为" + res);
    }

    @GetMapping(value = "/avg")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getCodeBlockAvgScore(@RequestParam("repoName") String repoName, @RequestParam("tag") String tag, @RequestParam("isTop") boolean isTop, @RequestParam("count") int count) {
        return AjaxResult.success(detailService.selectCodeBlockVOTopOrBot(repoName, tag, isTop, count));
    }

    @GetMapping(value = "/base")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getBaseTag(@RequestParam("userName") String userName, @RequestParam("repoName") String repoName, @RequestParam("last") String last) {
        return AjaxResult.success(detailService.getBaseTag(userName, repoName, last));
    }

    @GetMapping(value = "/aggregation")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getCodeBlockAggregation(@RequestParam("repoName") String repoName) {
        return AjaxResult.success(detailService.getCodeBlockAggregation(repoName));
    }

    @GetMapping(value = "/core")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getCoreCodeBlock(@RequestParam("repoName") String repoName, @RequestParam("tag") String tag) {
        return AjaxResult.success(detailService.getCoreCodeBlock(repoName, tag));
    }

    @GetMapping(value = "/file")
    @PreAuthorize("@ss.hasAnyPermi('analyze:detail')")
    public AjaxResult getCodeFile(@RequestParam("userName") String userName, @RequestParam("repoName") String repoName, @RequestParam("tag") String tag, @RequestParam("path") String path) {
        return AjaxResult.success(detailService.getCodeFile(userName, repoName, tag, path));
    }
}
