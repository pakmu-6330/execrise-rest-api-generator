package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * 模块的工程模型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectModel implements Serializable, UUIDIdentifier {

    private String id;
    private BasicInfoModel basicInfo;
    private DBInfoModel dbInfo;
    private IdArrayList<EntityModel> entityList;
    private IdArrayList<RelationshipModel> relationshipList;

    public static class IdArrayList<E> extends ArrayList<E> implements UUIDIdentifier {

        private String id;

        public IdArrayList(int initialCapacity) {
            super(initialCapacity);

            this.id = UUID.randomUUID().toString();
        }

        public IdArrayList() {
            super();

            this.id = UUID.randomUUID().toString();
        }

        public IdArrayList(Collection<? extends E> c) {
            super(c);

            this.id = UUID.randomUUID().toString();
        }

        @Override
        public String getId() {
            return this.id;
        }
    }

    public ProjectModel() {
        this.basicInfo = new BasicInfoModel();
        this.dbInfo = new DBInfoModel();
        this.entityList = new IdArrayList<>();
        this.relationshipList = new IdArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    public BasicInfoModel getBasicInfo() {
        return basicInfo;
    }

    public ProjectModel setBasicInfo(BasicInfoModel basicInfo) {
        this.basicInfo = basicInfo;
        return this;
    }

    public DBInfoModel getDbInfo() {
        return dbInfo;
    }

    public ProjectModel setDbInfo(DBInfoModel dbInfo) {
        this.dbInfo = dbInfo;
        return this;
    }

    public IdArrayList<EntityModel> getEntityList() {
        return entityList;
    }

    public ProjectModel setEntityList(List<EntityModel> entityList) {
        if (entityList instanceof IdArrayList) {
            this.entityList = (IdArrayList<EntityModel>) entityList;
        } else {
            this.entityList.addAll(entityList);
        }

        return this;
    }

    public IdArrayList<RelationshipModel> getRelationshipList() {
        return relationshipList;
    }

    public ProjectModel setRelationshipList(List<RelationshipModel> relationshipList) {
        if (relationshipList instanceof IdArrayList) {
            this.relationshipList = (IdArrayList<RelationshipModel>) relationshipList;
        } else {
            this.relationshipList.addAll(relationshipList);
        }

        return this;
    }
}
