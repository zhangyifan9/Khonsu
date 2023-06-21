package cn.edu.nju.service;

import cn.edu.nju.domain.vo.ProjectVO;

import java.util.List;

/**
 * @InterfaceName: IndexService
 * @Description: TODO
 * @Author panpan
 */
public interface IndexService {
    List<ProjectVO> selectAllProject();

    void updateProject(Long id);
}
