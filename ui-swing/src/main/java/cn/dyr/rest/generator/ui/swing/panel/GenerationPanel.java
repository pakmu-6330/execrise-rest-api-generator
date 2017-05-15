package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.context.ProjectGenerator;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.UUID;

/**
 * 用于指定代码生成参数的页面
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class GenerationPanel extends JPanel implements ActionListener, UUIDIdentifier {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(GenerationPanel.class);
    }

    private String id;

    // <editor-fold desc="界面相关代码">

    private JTextField targetFileURL;
    private JTextField targetExecutableURL;
    private JFileChooser fileChooser;

    private JPanel outputPanel;
    private JPanel executableOutputPanel;

    private JButton generateButton;
    private JButton browserButton;
    private JButton executableGenerateButton;
    private JButton executableBrowserButton;

    private void initComponents() {
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        this.outputPanel = new JPanel(new GridLayout(3, 1));
        this.outputPanel.setBorder(BorderFactory.createTitledBorder("代码输出信息"));

        this.executableOutputPanel = new JPanel(new GridLayout(3, 1));
        this.executableOutputPanel.setBorder(BorderFactory.createTitledBorder("可执行文件输出"));

        this.generateButton = new JButton("生成");
        this.generateButton.addActionListener(this);

        this.executableGenerateButton = new JButton("生成");
        this.executableGenerateButton.addActionListener(this);

        this.browserButton = new JButton("浏览");
        this.browserButton.addActionListener(this);

        this.executableBrowserButton = new JButton("浏览");
        this.executableBrowserButton.addActionListener(this);

        this.targetFileURL = new JTextField();
        this.targetFileURL.setEditable(false);

        this.targetExecutableURL = new JTextField();
        this.targetExecutableURL.setEditable(false);

        // 代码输出的面板
        // 面板的第一行
        JLabel label = new JLabel("请选择输出的目标目录：");
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstRow.add(label);
        outputPanel.add(firstRow);

        // 第二行
        JPanel secondRow = new JPanel(new BorderLayout());
        secondRow.add(this.targetFileURL, BorderLayout.CENTER);
        secondRow.add(this.browserButton, BorderLayout.EAST);
        outputPanel.add(secondRow);

        // 第三行
        JPanel lastRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lastRow.add(this.generateButton);
        outputPanel.add(lastRow);

        // 可执行文件输出的面板
        // 第一行
        JLabel executable = new JLabel("请选择可执行文件的输出路径：");
        JPanel executableFirstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        executableFirstRow.add(executable);
        this.executableOutputPanel.add(executableFirstRow);

        // 第二行
        JPanel executableSecondRow = new JPanel(new BorderLayout());
        executableSecondRow.add(this.targetExecutableURL, BorderLayout.CENTER);
        executableSecondRow.add(this.executableBrowserButton, BorderLayout.EAST);
        this.executableOutputPanel.add(executableSecondRow);

        // 第三行
        JPanel executableLastRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        executableLastRow.add(this.executableGenerateButton);
        this.executableOutputPanel.add(executableLastRow);

        // 用于组织两个面板的临时面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(this.outputPanel);
        mainPanel.add(this.executableOutputPanel);

        this.add(mainPanel, BorderLayout.NORTH);
    }

    // </editor-fold>

    public GenerationPanel() {
        super(new BorderLayout());

        this.initComponents();

        this.id = UUID.randomUUID().toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.browserButton) {
            int result = this.fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = this.fileChooser.getSelectedFile().getAbsolutePath();

                this.targetFileURL.setText(path);
            }

        } else if (source == this.generateButton) {
            Thread thread = new Thread(new GenerateThread());
            thread.start();
        }
    }

    private void setInputEnabled(final boolean enabled) {
        EventQueue.invokeLater(() -> {
            browserButton.setEnabled(enabled);
            generateButton.setEnabled(enabled);
        });
    }

    @Override
    public String getId() {
        return this.id;
    }

    private class GenerateThread implements Runnable {

        @Override
        public void run() {
            String targetFileURLText = targetFileURL.getText();
            if (targetFileURLText == null || "".equals(targetFileURLText.trim())) {
                EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(GenerationPanel.this, "请输入有效的路径", "REST API Generator", JOptionPane.ERROR_MESSAGE));
                return;
            }

            File targetDir = new File(targetFileURLText);
            if (targetDir.exists() && !targetDir.isDirectory()) {
                EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(
                        GenerationPanel.this, "该路径不能用作输出路径，请重新选择", "REST API Generator", JOptionPane.ERROR_MESSAGE));
                return;
            }

            try {
                setInputEnabled(false);

                ProjectGenerator generator = new ProjectGenerator(SwingUIApplication.getInstance().getCurrentProjectContext());
                generator.generate(targetDir);

                EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(GenerationPanel.this, "代码生成成功！"));
            } catch (Exception e) {
                logger.error("exception occurred during generating code...", e);
            } finally {
                setInputEnabled(true);
            }
        }
    }
}
