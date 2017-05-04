package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 这个是一个用户对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private int status;

    @OneToMany(mappedBy = "submitter")
    private List<JobEntity> jobs;

    @OneToMany(mappedBy = "user")
    private List<ProjectEntity> projects;

    public long getId() {
        return id;
    }

    public UserEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public UserEntity setStatus(int status) {
        this.status = status;
        return this;
    }

    public List<JobEntity> getJobs() {
        return jobs;
    }

    public UserEntity setJobs(List<JobEntity> jobs) {
        this.jobs = jobs;
        return this;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public UserEntity setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
        return this;
    }
}
