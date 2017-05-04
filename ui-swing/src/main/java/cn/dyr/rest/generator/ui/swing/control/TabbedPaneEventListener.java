package cn.dyr.rest.generator.ui.swing.control;

import java.awt.Component;

/**
 * 用于监听面板的关闭和打开功能
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface TabbedPaneEventListener {

    void onTabAdd(Component component);

    void onTabRemoved(Component component);

}
