package cn.dyr.rest.generator.ui.web.common.factory;

import cn.dyr.rest.generator.ui.web.common.ResponseMeta;
import cn.dyr.rest.generator.ui.web.constant.ResponseConstants;

/**
 * 响应元信息工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ResponseMetaFactory {

    /**
     * 创建一个表示操作完成的响应元信息
     *
     * @return 响应元信息
     */
    public static ResponseMeta okMeta() {
        ResponseMeta meta = new ResponseMeta();
        meta.setCode(ResponseConstants.RESPONSE_OK);
        meta.setMessage("OK");

        return meta;
    }

    /**
     * 创建一个参数问题的响应元数据
     *
     * @param desc 问题的描述字符串
     * @return 响应元信息
     */
    public static ResponseMeta badParameterMeta(String desc) {
        ResponseMeta meta = new ResponseMeta();
        meta.setCode(ResponseConstants.BAD_PARAMETER);
        meta.setMessage("BAD_PARAMETER");
        meta.setDetailed(desc);

        return meta;
    }

    /**
     * 创建一个表示资源或者其他条件冲突的响应元信息
     *
     * @param detailMsg 详情
     * @return 对应的响应的元信息
     */
    public static ResponseMeta duplicateMeta(String detailMsg) {
        ResponseMeta meta = new ResponseMeta();

        meta.setCode(ResponseConstants.RESPONSE_DUPLICATE);
        meta.setMessage("DUPLICATED_ENTITY");
        meta.setDetailed(detailMsg);

        return meta;
    }

    /**
     * 创建一个表示 token 无效或者 token 超时的响应元信息
     *
     * @return 对应的响应元信息
     */
    public static ResponseMeta expiredMeta() {
        ResponseMeta meta = new ResponseMeta();

        meta.setCode(ResponseConstants.TOKEN_EXPIRED);
        meta.setMessage("TOKEN_EXPIRED");

        return meta;
    }

    /**
     * 创建一个权限认证失败的响应元数据
     *
     * @return 对应的响应元数据
     */
    public static ResponseMeta authFailed() {
        ResponseMeta meta = new ResponseMeta();

        meta.setCode(ResponseConstants.TOKEN_AUTH_FAILED);
        meta.setMessage("AUTH_FAILED");

        return meta;
    }

    /**
     * 创建一个没有找到资源的响应元数据
     *
     * @return 对应的响应元数据
     */
    public static ResponseMeta notFound() {
        ResponseMeta meta = new ResponseMeta();

        meta.setCode(ResponseConstants.NOT_FOUND);
        meta.setMessage("NOT_FOUND");

        return meta;
    }

    /**
     * 创建一个表示不必要的响应元数据
     *
     * @return 对应的响应元数据
     */
    public static ResponseMeta unnecessary() {
        ResponseMeta meta = new ResponseMeta();

        meta.setCode(ResponseConstants.UNNECESSARY);
        meta.setMessage("UNNECESSARY");

        return meta;
    }
}
