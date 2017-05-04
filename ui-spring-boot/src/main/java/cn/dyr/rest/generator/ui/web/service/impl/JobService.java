package cn.dyr.rest.generator.ui.web.service.impl;

import cn.dyr.rest.generator.ui.web.component.IGeneratorService;
import cn.dyr.rest.generator.ui.web.constant.EntityConstants;
import cn.dyr.rest.generator.ui.web.dao.IJobDAO;
import cn.dyr.rest.generator.ui.web.dao.IProjectDAO;
import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.exception.ResourceNotFoundException;
import cn.dyr.rest.generator.ui.web.service.IJobService;
import cn.dyr.rest.generator.ui.web.util.LazyLoadProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 代码生成任务的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Service
public class JobService implements IJobService {

    @Autowired
    private IGeneratorService generatorService;

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IJobDAO jobDAO;

    @Override
    public JobEntity submitJob(String projectId, UserEntity userEntity) {
        // 1. 检查工程是否存在
        ProjectEntity projectEntity = this.projectDAO.findByProjectId(projectId);
        if (projectEntity == null) {
            throw new ResourceNotFoundException("project with id " + projectId + " not found");
        }

        // 2. 组装任务对象
        JobEntity jobEntity = new JobEntity();
        jobEntity.setSubmitter(userEntity);
        jobEntity.setSubmitTime(new Date());
        jobEntity.setJobStatus(EntityConstants.JobEntityConstant.JOB_STATUS_ACCEPT);
        jobEntity.setProject(projectEntity);
        jobEntity.setName(projectId);

        // 3. 写入到数据库
        jobDAO.save(jobEntity);

        projectEntity.setStatus(EntityConstants.ProjectEntityConstant.TYPE_SUBMITTED);
        projectEntity.setJob(jobEntity);
        projectDAO.save(projectEntity);

        // 4. 解决懒加载的问题
        LazyLoadProcessor.processProjectEntity(projectEntity);

        // 5. 通知内核进行代码生成
        generatorService.submitJob(jobEntity);

        return jobEntity;
    }

}
