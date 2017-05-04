package cn.dyr.rest.generator.framework.swagger;

import cn.dyr.rest.generator.xml.maven.MavenCoordinate;
import cn.dyr.rest.generator.xml.maven.MavenDependency;

/**
 * Swagger Maven 相关的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SwaggerMavenFactory {

    private static final String SPRING_FOX_GROUP_ID = "io.springfox";
    private static final String SWAGGER_2_ARTIFACT_ID = "springfox-swagger2";
    private static final String SWAGGER_UI_ARTIFACT_ID = "springfox-swagger-ui";

    private static final String VERSION_STRING_2_6_1 = "2.6.1";

    /**
     * 产生一个 swagger 依赖结点
     *
     * @return Swagger 依赖 dependency 结点
     */
    public static MavenDependency swagger() {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(SPRING_FOX_GROUP_ID)
                .setArtifactId(SWAGGER_2_ARTIFACT_ID)
                .setVersion(VERSION_STRING_2_6_1);

        return new MavenDependency().setCoordinate(coordinate);
    }

    /**
     * 产生一个 swagger ui 依赖结点
     *
     * @return Swagger 依赖 dependency 结点
     */
    public static MavenDependency swaggerUi() {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(SPRING_FOX_GROUP_ID)
                .setArtifactId(SWAGGER_UI_ARTIFACT_ID)
                .setVersion(VERSION_STRING_2_6_1);

        return new MavenDependency().setCoordinate(coordinate);
    }

}
