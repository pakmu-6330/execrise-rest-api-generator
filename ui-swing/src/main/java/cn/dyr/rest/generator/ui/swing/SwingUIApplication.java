package cn.dyr.rest.generator.ui.swing;

import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.frame.MainWindow;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.persist.XMLAdapter;
import cn.dyr.rest.generator.ui.swing.persist.XMLProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个类是 Swing UI 界面的入口类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SwingUIApplication {
    private static SwingUIApplication INSTANCE;
    private static Logger logger;

    public static final String APP_NAME = "REST API Generator Swing UI";

    static {
        INSTANCE = new SwingUIApplication();
        logger = LoggerFactory.getLogger(SwingUIApplication.class);
    }

    /**
     * 获得这个类的唯一实例
     *
     * @return 这个类的唯一实例
     */
    public static SwingUIApplication getInstance() {
        return INSTANCE;
    }

    private ExecutorService backgroundThreads;
    private ProjectModel projectModel;
    private ProjectContext currentProjectContext;

    private SwingUIApplication() {
        backgroundThreads = Executors.newFixedThreadPool(5);
    }

    public static void main(String[] args) {
        SwingUIApplication.getInstance().runBackground(() -> {

        });

        INSTANCE.createProject();

        // 这里进行字体的注册
        FontUIResource fontUIResource = new FontUIResource(new Font("宋体", Font.PLAIN, 12));
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }

        EventQueue.invokeLater(() -> {
            MainWindow frm = new MainWindow();
            frm.display();
        });
    }

    public void runBackground(Runnable execute) {
        if (execute == null) {
            return;
        }

        backgroundThreads.execute(new RunnableWrapper(execute));
    }

    public void runAndcallback(Runnable execute, Runnable callback) {
        RunAndCallback cmd = new RunAndCallback(execute, callback);
        backgroundThreads.execute(cmd);
    }

    public ProjectModel createProject() {
        this.projectModel = new ProjectModel();
        this.currentProjectContext = new ProjectContext(this.projectModel);

        return this.projectModel;
    }

    /**
     * 将当前的工程数据保存到文件当中
     *
     * @param file 目标文件
     */
    public void saveProjectToFile(File file) throws JAXBException, IOException {
        XMLProject xmlProject = XMLAdapter.fromModel(projectModel);

        JAXBContext jaxbContext = JAXBContext.newInstance(XMLProject.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        marshaller.marshal(xmlProject, fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * 从文件中读取一个工程数据
     *
     * @param file 文件
     * @throws JAXBException 如果文件解析出错，则会抛出这个异常
     */
    public void loadProjectFromFile(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLProject.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        XMLProject xmlProject = (XMLProject) unmarshaller.unmarshal(file);

        ProjectContext projectContext = XMLAdapter.fromXml(xmlProject);

        this.projectModel = projectContext.getProjectModel();
        this.currentProjectContext = projectContext;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public ProjectContext getCurrentProjectContext() {
        return this.currentProjectContext;
    }

    private final class RunnableWrapper implements Runnable {

        private Runnable body;

        public RunnableWrapper(Runnable body) {
            this.body = body;
        }

        @Override
        public void run() {
            body.run();
        }
    }

    private final class RunAndCallback implements Runnable {

        private Runnable execute;
        private Runnable callback;

        public RunAndCallback(Runnable execute, Runnable callback) {
            this.execute = execute;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                if (execute != null) {
                    execute.run();
                }
            } catch (Exception e) {
                logger.error("exception during executing background task...", e);
            } finally {
                if (callback != null) {
                    callback.run();
                }
            }
        }
    }
}
