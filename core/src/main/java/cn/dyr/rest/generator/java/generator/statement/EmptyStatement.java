package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;

/**
 * 表示一条空行
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EmptyStatement implements IStatement {

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        return indentManager.getIndentString() + ElementsConstant.LINE_SEPARATOR;
    }
}
