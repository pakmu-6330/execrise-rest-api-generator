package cn.dyr.rest.generator.xml;

import org.dom4j.Branch;

/**
 * 这个接口用在可以被按着特定规律转化成 xml 结点的的对象当中，实现接口时需要对转换的规则进行相应的实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IXMLMapping {

    void xmlMapping(Branch parentBranch);

}
