package cn.dyr.rest.generator.ui.web.pages.controller;

import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.FromToken;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 工程页面相关的控制器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Controller
@RequestMapping("/page/projects")
public class PageProjectController {

    @Autowired
    private IProjectService projectService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getProjects(
            @PageableDefault(size = 5) Pageable pageable,
            @FromToken TokenSession tokenSession,
            Map<String, Object> model) {
        Page<ProjectEntity> projects = projectService.getProjects(tokenSession.getUserEntity(), pageable);
        model.put("page", projects);

        return "function/project-list";
    }

}
