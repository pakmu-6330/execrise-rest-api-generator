package cn.dyr.rest.generator.ui.swing.control;

/**
 * 这个接口用于表示这个面板上面承载这数据，用户关闭的时候会询问其是否进行保存
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface DataPanel {

    /**
     * 判断这个面板上面承载的数据是否已经被修改
     *
     * @return 表示面板承载的数据是否已经被修改
     */
    boolean isDirty();

    /**
     * 对这个面板上面承载数据的修改位进行修改
     */
    void setDirty(boolean dirty);

    /**
     * 保存数据
     *
     * @return 执行保存操作的 runnable 对象
     */
    Runnable saveData();
}
