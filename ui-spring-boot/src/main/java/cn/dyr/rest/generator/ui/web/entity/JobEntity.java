package cn.dyr.rest.generator.ui.web.entity;

import cn.dyr.rest.generator.ui.web.constant.EntityConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表示一个工程生成任务
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "JOB")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private Date submitTime;

    @Column
    private Date finishTime;

    @Column
    private int jobStatus;

    @OneToOne(mappedBy = "job")
    private ProjectEntity project;

    @ManyToOne
    private UserEntity submitter;

    public JobEntity() {
        this.jobStatus = EntityConstants.JobEntityConstant.JOB_STATUS_ACCEPT;
    }

    public long getId() {
        return id;
    }

    public JobEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public JobEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public JobEntity setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public JobEntity setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public JobEntity setProject(ProjectEntity project) {
        this.project = project;
        return this;
    }

    public UserEntity getSubmitter() {
        return submitter;
    }

    public JobEntity setSubmitter(UserEntity submitter) {
        this.submitter = submitter;
        return this;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public JobEntity setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }
}
