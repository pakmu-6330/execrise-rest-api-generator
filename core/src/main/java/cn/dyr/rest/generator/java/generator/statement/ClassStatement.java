package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.AnnotationToken;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.ModifierInfoToken;
import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.ConstructorMethodInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.util.CommaStringBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 这个表示的是一个类的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassStatement implements IStatement {

    private String className;

    private ClassInfo classInfo;

    private List<IStatement> fieldDeclarationStatements;
    private List<IStatement> methodStatements;
    private List<IStatement> constructorMethodStatements;

    private List<AnnotationToken> annotationTokens;

    public ClassStatement(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.className = this.classInfo.getClassName();

        this.fieldDeclarationStatements = new ArrayList<>();
        this.methodStatements = new ArrayList<>();
        this.constructorMethodStatements = new ArrayList<>();

        this.annotationTokens = new ArrayList<>();

        // 对字段声明的语句进行迭代
        Iterator<FieldInfo> fieldInfoIterator = classInfo.iterateFields();
        while (fieldInfoIterator.hasNext()) {
            FieldInfo fieldInfo = fieldInfoIterator.next();
            FieldDeclarationStatement fieldStatement = new FieldDeclarationStatement(fieldInfo);

            this.fieldDeclarationStatements.add(fieldStatement);
        }

        // 对注解信息进行迭代
        Iterator<AnnotationInfo> annotationInfoIterator = classInfo.iterateAnnotations();
        while (annotationInfoIterator.hasNext()) {
            AnnotationInfo annotationInfo = annotationInfoIterator.next();

            AnnotationToken annotationToken = new AnnotationToken(annotationInfo);
            this.annotationTokens.add(annotationToken);
        }

        // 对构造方法进行迭代
        Iterator<ConstructorMethodInfo> constructorMethodInfoIterator = classInfo.iterateConstructorMethods();
        while (constructorMethodInfoIterator.hasNext()) {
            ConstructorMethodInfo methodInfo = constructorMethodInfoIterator.next();
            MethodStatement statement = new MethodStatement(methodInfo);

            this.constructorMethodStatements.add(statement);
        }

        // 对类当中的方法进行进行迭代
        Iterator<MethodInfo> methodInfoIterator = classInfo.iterateMethods();
        while (methodInfoIterator.hasNext()) {
            MethodInfo methodInfo = methodInfoIterator.next();
            MethodStatement statement = new MethodStatement(methodInfo);

            this.methodStatements.add(statement);
        }
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();
        StringBuilder code = new StringBuilder();

        // 1. 生成注解
        for (AnnotationToken annotationToken : this.annotationTokens) {
            code.append(annotationToken.toString());
            code.append(ElementsConstant.LINE_SEPARATOR);
        }

        // 2. 生成类本身的信息
        String suffix = "";
        String parentClass = "";
        TypeInfo parentType = this.classInfo.getParent();
        if (!parentType.equals(TypeInfoFactory.objectType())) {
            parentClass = "extends " + new TypeInfoToken(parentType).toString();
            suffix = parentClass;
        }

        String interfaceString = "";
        Set<TypeInfo> interfaces = this.classInfo.getInterfaces();
        if (interfaces != null && interfaces.size() > 0) {
            CommaStringBuilder builder = new CommaStringBuilder();

            for (TypeInfo interfaceInto : interfaces) {
                TypeInfoToken typeInfoToken = new TypeInfoToken(interfaceInto);
                builder.addValue(typeInfoToken.toString());
            }

            interfaceString = "implements " + builder.toString();

            if (suffix.length() != 0) {
                suffix = suffix + " " + interfaceString;
            } else {
                suffix = interfaceString;
            }
        }

        if (suffix.length() > 0) {
            suffix = suffix + " ";
        }

        ModifierInfoToken modifierInfoToken = new ModifierInfoToken(this.classInfo.getModifier());

        // 生成类名和相应的泛型信息
        String classTitle = this.className;
        List<TypeInfo> genericPlaceHolders = this.classInfo.getGenericPlaceHolders();
        if (genericPlaceHolders != null && genericPlaceHolders.size() > 0) {
            CommaStringBuilder builder = new CommaStringBuilder();
            for (TypeInfo typeInfo : genericPlaceHolders) {
                builder.addValue(typeInfo.getName());
            }

            classTitle = classTitle + "<" + builder.toString() + ">";
        }

        // 处理代码的缩进
        code.append(indentManager.getIndentString());
        code.append(String.format("%s%s %s %s{",
                indentManager.getIndentString(),
                modifierInfoToken.generateClassModifierString(),
                classTitle, suffix));
        code.append(ElementsConstant.LINE_SEPARATOR);

        // 3. 格式控制：代码缩进一级
        indentManager.indent();

        // 4. 遍历字段声明语句
        for (IStatement declarationStatement : this.fieldDeclarationStatements) {
            String statementStr = declarationStatement.toString();

            code.append(statementStr);
            code.append(ElementsConstant.LINE_SEPARATOR);
        }

        code.append(ElementsConstant.LINE_SEPARATOR);

        // 5. 遍历构造方法
        for (IStatement constructorStatement : this.constructorMethodStatements) {
            String methodStr = constructorStatement.toString();

            code.append(methodStr);
            code.append(ElementsConstant.LINE_SEPARATOR);
            code.append(ElementsConstant.LINE_SEPARATOR);
        }

        // 6. 迭代所有方法，生成相应的语句
        for (IStatement methodStatement : this.methodStatements) {
            String methodStr = methodStatement.toString();

            code.append(methodStr);
            code.append(ElementsConstant.LINE_SEPARATOR);
            code.append(ElementsConstant.LINE_SEPARATOR);
        }

        // 7. 格式控制：减少代码缩进一级
        indentManager.delIndent();

        // 8. 生成类最后的大括号
        code.append(indentManager.getIndentString());
        code.append("}");
        code.append(ElementsConstant.LINE_SEPARATOR);

        return code.toString();
    }
}