package cn.dyr.rest.generator.ui.web.exception;

/**
 * 尝试对一个提交生成的项目进行修改或者删除时会抛出这个异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class WrongProjectStatus extends RuntimeException {

    private String desc;

    public WrongProjectStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public WrongProjectStatus setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
