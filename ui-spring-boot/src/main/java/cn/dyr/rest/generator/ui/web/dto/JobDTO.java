package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 表示一个代码生成作业的 DTO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JobDTO {

    private String name;
    private Date submitTime;
    private Date finishTime;
    private int jobStatus;
    private String projectId;
    private UserDTO submitter;

    public static JobDTO fromEntity(JobEntity entity) {
        JobDTO dto = new JobDTO();

        BeanUtils.copyProperties(entity, dto, "submitter");
        UserDTO submitter = UserDTO.fromUserEntity(entity.getSubmitter());
        dto.setSubmitter(submitter);

        return dto;
    }

    public String getName() {
        return name;
    }

    public JobDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public JobDTO setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public JobDTO setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public JobDTO setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public JobDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public UserDTO getSubmitter() {
        return submitter;
    }

    public JobDTO setSubmitter(UserDTO submitter) {
        this.submitter = submitter;
        return this;
    }
}
