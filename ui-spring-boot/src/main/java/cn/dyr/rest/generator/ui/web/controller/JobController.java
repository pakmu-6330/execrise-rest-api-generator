package cn.dyr.rest.generator.ui.web.controller;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.AuthMethod;
import cn.dyr.rest.generator.ui.web.common.auth.AuthType;
import cn.dyr.rest.generator.ui.web.common.auth.FromToken;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.dto.JobDTO;
import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import cn.dyr.rest.generator.ui.web.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码生成作业相关的 Controller
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private IJobService jobService;

    @AuthMethod(AuthType.VERIFIED)
    @PostMapping("/{projectId}")
    public HttpEntity<APIResponse<JobDTO>> submitJob(
            @PathVariable("projectId") String projectId,
            @FromToken TokenSession session
    ) {
        JobEntity jobEntity = this.jobService.submitJob(projectId, session.getUserEntity());
        JobDTO jobDTO = JobDTO.fromEntity(jobEntity);
        APIResponse<JobDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), jobDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
