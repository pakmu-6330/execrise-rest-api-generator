package cn.dyr.rest.generator.util;

/**
 * 包相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PackageUtils {

    /**
     * 获取基包名
     *
     * @param rootPackage 基包名
     * @return 经过防空处理的基包名
     */
    public static String getRootPackageName(String rootPackage) {
        if (rootPackage != null && rootPackage.trim().length() > 0) {
            return rootPackage;
        } else {
            return "";
        }
    }

    /**
     * 获得子包包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 最终的包名
     */
    public static String getSubPackage(String parent, String subPackage) {
        if (parent != null && parent.trim().length() > 0) {
            return parent + "." + subPackage;
        } else {
            return subPackage;
        }
    }

}
