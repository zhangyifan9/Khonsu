package cn.edu.nju.web.controller.index;

import cn.edu.nju.common.core.domain.AjaxResult;
import cn.edu.nju.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: IndexController
 * @Description: TODO
 * @Author panpan
 */
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    IndexService indexService;

    @GetMapping(value = "/project")
    public AjaxResult getProject() {
        return AjaxResult.success(indexService.selectAllProject());
    }

    @GetMapping(value = "/update")
    public AjaxResult updateProject(@RequestParam("id") Long id) {
        indexService.updateProject(id);
        return AjaxResult.success("更新成功");
    }
}
