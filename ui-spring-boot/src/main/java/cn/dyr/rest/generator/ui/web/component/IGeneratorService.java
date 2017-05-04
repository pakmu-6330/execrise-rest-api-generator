package cn.dyr.rest.generator.ui.web.component;

import cn.dyr.rest.generator.ui.web.entity.JobEntity;

/**
 * 这个接口定义了代码生成内核对上层暴露的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IGeneratorService {

    /**
     * 往代码生成的内核提交一个代码生成作业
     *
     * @param jobEntity 要提交到代码生成内核当中的代码生成作业
     */
    void submitJob(JobEntity jobEntity);

}
