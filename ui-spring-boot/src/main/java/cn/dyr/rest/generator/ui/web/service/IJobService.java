package cn.dyr.rest.generator.ui.web.service;

import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;

/**
 * 代码生成作业相关 Service 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IJobService {

    /**
     * 提交一个项目生成的作业
     *
     * @param projectId  工程名称
     * @param userEntity 提交这个生成作业的用户
     * @return 这个工程对应的 JobEntity
     */
    JobEntity submitJob(String projectId, UserEntity userEntity);

}
