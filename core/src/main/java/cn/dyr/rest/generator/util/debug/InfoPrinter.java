package cn.dyr.rest.generator.util.debug;

/**
 * 用于打印调用栈信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class InfoPrinter {

    public static void printStackTrace() {
        Throwable throwable = new Throwable();
        throwable.printStackTrace();
    }

}
