package cn.edu.nju.service.impl;

import cn.edu.nju.domain.vo.TagVO;
import cn.edu.nju.domain.vo.TagWithDateVO;
import cn.edu.nju.mapper.ProjectMapper;
import cn.edu.nju.service.NewService;
import cn.edu.nju.util.GitHubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName: ImportServiceImpl
 * @Description: TODO
 * @Author panpan
 */
@Service
public class NewServiceImpl implements NewService {
    @Value("${nju.token}")
    private String token;

    @Value("${nju.cloneToken}")
    private String cloneToken;

    @Value("${nju.path}")
    private String path;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public void createProject(String userName, String repoName) {
        if (userName.length() > 0 && repoName.length() > 0) {
            projectMapper.insertProject(userName, repoName);
        }
    }

    @Override
    public void deleteProject(String userName, String repoName) {
        projectMapper.deleteProjectByRepoName(repoName);
    }


    @Override
    public List<TagWithDateVO> getTagWithDateList(String userName, String repoName) {
        List<TagWithDateVO> tagWithDateVOs = new ArrayList<>();
        try {
            tagWithDateVOs.addAll(GitHubUtil.getTagWithDateList(userName, repoName, token));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tagWithDateVOs;
    }

    @Override
    public List<TagVO> getTagVOList(String userName, String repoName, String tag1, String tag2) {
        List<String> descendTag = new ArrayList<>();
        descendTag.add(tag1);
        descendTag.add(tag2);
        Collections.sort(descendTag, Comparator.comparingInt(a -> GitHubUtil.sortedTags.indexOf(a)));
        List<TagVO> tagVOs = new ArrayList<>();
        try {
            tagVOs.addAll(GitHubUtil.getTagInfo(userName, repoName, descendTag.get(0), descendTag.get(1), token));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tagVOs;
    }

    @Override
    public boolean downloadSpecificVersions(String userName, String repoName, List<String> tags) {
        return GitHubUtil.downloadSpecificVersions(userName, repoName, tags, path, cloneToken);
    }

    @Override
    public List<String> getRNs(String userName, String repoName, List<String> tags) {
        List<String> rns = new ArrayList<>();
        Collections.sort(tags, Comparator.comparingInt(a -> GitHubUtil.sortedTags.indexOf(a)));
        if (tags.size() == 1 && tags.get(0).length() == 0) {
            rns.add("没有选择版本");
            return rns;
        }
        try {
            for (String tag : tags) {
                rns.add(GitHubUtil.getReleaseNote(userName, repoName, tag, token));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rns;
    }
}
