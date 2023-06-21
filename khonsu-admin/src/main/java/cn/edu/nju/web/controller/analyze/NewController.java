package cn.edu.nju.web.controller.analyze;

import cn.edu.nju.common.core.controller.BaseController;
import cn.edu.nju.common.core.domain.AjaxResult;
import cn.edu.nju.domain.request.AnalyzeRequest;
import cn.edu.nju.service.AnalyzeService;
import cn.edu.nju.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @ClassName: AnalyzeController
 * @Description: TODO
 * @Author panpan
 */
@RestController
@RequestMapping("/analyze/new")
public class NewController extends BaseController {

    @Autowired
    private NewService newService;

    @Autowired
    private AnalyzeService analyzeService;


    @GetMapping(value = "/show")
    @PreAuthorize("@ss.hasAnyPermi('analyze:new')")
    public AjaxResult getTagList(@RequestParam("userName") String userName, @RequestParam("repoName") String repoName) {
        return AjaxResult.success(newService.getTagWithDateList(userName, repoName));
    }

    @GetMapping(value = "/tag")
    @PreAuthorize("@ss.hasAnyPermi('analyze:new')")
    public AjaxResult getTagInfo(@RequestParam("userName") String userName, @RequestParam("repoName") String repoName, @RequestParam("tag1") String tag1, @RequestParam("tag2") String tag2) {
        return AjaxResult.success(newService.getTagVOList(userName, repoName, tag1, tag2));
    }

    @GetMapping(value = "/rn")
    @PreAuthorize("@ss.hasAnyPermi('analyze:new')")
    public AjaxResult getRNs(@RequestParam("userName") String userName, @RequestParam("repoName") String repoName, @RequestParam("tags") String tags) {
        return AjaxResult.success(newService.getRNs(userName, repoName, Arrays.asList(tags.split("&"))));
    }

    @PostMapping(value = "/start")
    @PreAuthorize("@ss.hasAnyPermi('analyze:new')")
    public AjaxResult startAnalyze(@RequestBody AnalyzeRequest request) {
        boolean isContinue = newService.downloadSpecificVersions(request.getUserName(), request.getRepoName(), Arrays.asList(request.getTags().split("&")));
        if (!isContinue)
            return AjaxResult.error("至少留一个版本作为base");
        newService.deleteProject(request.getUserName(), request.getRepoName());
        newService.createProject(request.getUserName(), request.getRepoName());
        analyzeService.analyzeProject(request.getRns(), request.getRepoName());
        return AjaxResult.success("分析完成");
    }
}
