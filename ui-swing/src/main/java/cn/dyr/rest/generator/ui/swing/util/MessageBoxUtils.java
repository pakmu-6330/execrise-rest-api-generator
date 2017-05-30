package cn.dyr.rest.generator.ui.swing.util;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 一些 Swing 相关的辅助类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MessageBoxUtils {

    public static void showExceptionMessageBox(Component parent, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        JTextArea body = new JTextArea();
        body.setEditable(false);
        body.setText(writer.toString());

        JOptionPane.showMessageDialog(parent, new JScrollPane(body), "exception", JOptionPane.ERROR_MESSAGE);
    }

}
