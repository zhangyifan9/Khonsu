package cn.edu.nju.service.impl;

import cn.edu.nju.domain.vo.ProjectVO;
import cn.edu.nju.mapper.ProjectMapper;
import cn.edu.nju.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: IndexServiceImpl
 * @Description: TODO
 * @Author panpan
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public List<ProjectVO> selectAllProject() {
        return projectMapper.selectAllProject();
    }

    @Override
    public void updateProject(Long id) {
        projectMapper.updateProjectById(id);
    }
}
