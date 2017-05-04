package cn.dyr.rest.generator.ui.web.controller;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.AuthMethod;
import cn.dyr.rest.generator.ui.web.common.auth.AuthType;
import cn.dyr.rest.generator.ui.web.common.auth.FromToken;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.dto.AttributeDTO;
import cn.dyr.rest.generator.ui.web.dto.DBInfoDTO;
import cn.dyr.rest.generator.ui.web.dto.EntityDTO;
import cn.dyr.rest.generator.ui.web.dto.ProjectDTO;
import cn.dyr.rest.generator.ui.web.dto.RelationshipDTO;
import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import cn.dyr.rest.generator.ui.web.service.IProjectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是项目相关的 Controller
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@RequestMapping("/projects")
@RestController
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    // --- 创建工程基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @PostMapping("")
    @ApiOperation(value = "创建一个工程项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectEntity", value = "项目信息", required = true)
    })
    public HttpEntity<APIResponse<ProjectDTO>> create(ProjectEntity projectEntity, @FromToken TokenSession session) {
        ProjectEntity project = this.projectService.createProject(projectEntity, session.getUserEntity());

        return new ResponseEntity<>(
                new APIResponse<>(ResponseMetaFactory.okMeta(),
                        ProjectDTO.fromProjectEntity(project)),
                HttpStatus.CREATED);
    }

    // --- 删除工程的基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @DeleteMapping("/{projectId}")
    public HttpEntity<APIResponse<ProjectDTO>> deleteProject(
            @PathVariable("projectId") String projectId
    ) {
        ProjectEntity projectEntity = projectService.deleteProject(projectId);
        ProjectDTO projectDTO = ProjectDTO.fromProjectEntity(projectEntity);
        APIResponse<ProjectDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), projectDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 查看工程的基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}")
    public HttpEntity<APIResponse<ProjectDTO>> getProject(
            @PathVariable("projectId") String projectId
    ) {
        ProjectEntity project = projectService.getProject(projectId);

        ProjectDTO dto = ProjectDTO.fromProjectEntity(project);
        return new ResponseEntity<>(new APIResponse<>(ResponseMetaFactory.okMeta(), dto), HttpStatus.OK);
    }

    // --- 创建和修改工程的数据库基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @RequestMapping(value = "/{projectId}/db", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpEntity<APIResponse<DBInfoDTO>> saveDBInfo(DBInfoEntity dbInfoEntity,
                                                         @PathVariable("projectId") String projectId) {
        DBInfoEntity saved = projectService.saveDBInfo(projectId, dbInfoEntity);
        DBInfoDTO dbInfoDTO = DBInfoDTO.fromDBInfoEntity(dbInfoEntity);
        dbInfoDTO.setProjectId(projectId);

        APIResponse<DBInfoDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dbInfoDTO);

        if (saved == null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    // --- 删除工程的数据库基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @DeleteMapping("/{projectId}/db")
    public HttpEntity<APIResponse<DBInfoDTO>> deleteDbInfo(
            @PathVariable("projectId") String projectId
    ) {
        DBInfoEntity dbInfoEntity = projectService.deleteDBInfo(projectId);
        DBInfoDTO dbInfoDTO = DBInfoDTO.fromDBInfoEntity(dbInfoEntity);
        APIResponse<DBInfoDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dbInfoDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 获得工程的数据库基本信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}/db")
    public HttpEntity<APIResponse<DBInfoDTO>> getDbInfo(
            @PathVariable("projectId") String projectId
    ) {
        DBInfoEntity dbInfo = projectService.getDBInfo(projectId);
        DBInfoDTO dto = DBInfoDTO.fromDBInfoEntity(dbInfo);
        APIResponse<DBInfoDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 创建和修改工程的实体信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @RequestMapping("/{projectId}/entities")
    public HttpEntity<APIResponse<EntityDTO>> saveEntityInfo(
            EntityEntity entityEntity,
            @PathVariable("projectId") String projectId
    ) {
        EntityEntity before = projectService.saveEntity(projectId, entityEntity);
        EntityDTO entityDTO = EntityDTO.fromEntityEntity(entityEntity);

        APIResponse<EntityDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), entityDTO);

        if (before == null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    // --- 查看工程所有的实体信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}/entities")
    public HttpEntity<APIResponse<List<EntityDTO>>> getEntities(
            @PathVariable String projectId
    ) {
        List<EntityEntity> entityList = projectService.getEntities(projectId);
        List<EntityDTO> dtoList = new ArrayList<>();

        for (EntityEntity entityEntity : entityList) {
            EntityDTO dto = EntityDTO.fromEntityEntity(entityEntity);
            dtoList.add(dto);
        }

        return new ResponseEntity<>(new APIResponse<>(ResponseMetaFactory.okMeta(), dtoList), HttpStatus.OK);
    }

    // --- 查看工程中某个特定的实体信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}/entities/{entityName}")
    public HttpEntity<APIResponse<EntityDTO>> getEntity(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName
    ) {
        EntityEntity entity = projectService.getEntity(projectId, entityName);
        APIResponse<EntityDTO> result = new APIResponse<>
                (ResponseMetaFactory.okMeta(),
                        EntityDTO.fromEntityEntity(entity));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 删除工程的实体信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @DeleteMapping("/{projectId}/entities/{entityName}")
    public HttpEntity<APIResponse<EntityDTO>> deleteEntity(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName
    ) {
        EntityEntity entity = projectService.deleteEntity(projectId, entityName);
        EntityDTO dto = EntityDTO.fromEntityEntity(entity);

        APIResponse<EntityDTO> response = new APIResponse<EntityDTO>(ResponseMetaFactory.okMeta(), dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // --- 创建和修改实体的属性信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @RequestMapping(value = "/{projectId}/entities/{entityName}/attributes", method = {RequestMethod.POST, RequestMethod.GET})
    public HttpEntity<APIResponse<AttributeDTO>> saveAttributeInfo(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName,
            AttributeEntity attributeEntity
    ) {
        AttributeEntity before = projectService.saveAttribute(projectId, entityName, attributeEntity);
        AttributeDTO dto = AttributeDTO.fromAttributeEntity(attributeEntity);

        APIResponse<AttributeDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dto);
        if (before == null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    // --- 删除实体的属性信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @DeleteMapping("/{projectId}/entities/{entityName}/attributes/{attribute}")
    public HttpEntity<APIResponse<AttributeDTO>> deleteAttributeInfo(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName,
            @PathVariable("attribute") String attribute
    ) {
        AttributeEntity entity = projectService.deleteAttribute(projectId, entityName, attribute);
        AttributeDTO dto = AttributeDTO.fromAttributeEntity(entity);

        APIResponse<AttributeDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 查看实体里面所有属性的信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}/entities/{entityName}/attributes")
    public HttpEntity<APIResponse<List<AttributeDTO>>> getAttributes(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName
    ) {
        List<AttributeEntity> attributes = projectService.getAttributes(projectId, entityName);
        List<AttributeDTO> dtoList = new ArrayList<>();
        for (AttributeEntity attribute : attributes) {
            AttributeDTO dto = AttributeDTO.fromAttributeEntity(attribute);
            dtoList.add(dto);
        }

        APIResponse<List<AttributeDTO>> response = new APIResponse<>(ResponseMetaFactory.okMeta(), dtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // --- 查看实体里面某个特定属性的信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @GetMapping("/{projectId}/entities/{entityName}/attributes/{attribute}")
    public HttpEntity<APIResponse<AttributeDTO>> getAttribute(
            @PathVariable("projectId") String projectId,
            @PathVariable("entityName") String entityName,
            @PathVariable("attribute") String attribute
    ) {
        AttributeEntity attributeEntity = projectService.getAttribute(projectId, entityName, attribute);
        AttributeDTO dto = AttributeDTO.fromAttributeEntity(attributeEntity);
        APIResponse<AttributeDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 保存和修改工程的关联关系信息 ---
    @AuthMethod(AuthType.VERIFIED)
    @RequestMapping(value = "/{projectId}/relationships", method = {RequestMethod.POST, RequestMethod.PUT})
    public HttpEntity<APIResponse<RelationshipDTO>> saveRelationship(
            @PathVariable("projectId") String projectId,
            RelationshipDTO relationshipDTO
    ) {
        RelationshipEntity relationshipEntity = this.projectService.saveRelationship(projectId, relationshipDTO);
        RelationshipDTO dto = RelationshipDTO.fromEntity(relationshipEntity);
        APIResponse<RelationshipDTO> result = new APIResponse<>(ResponseMetaFactory.okMeta(), dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // --- 获得工程当中所有的关联关系信息 ---

    // --- 获得工程当中某个特定的关联关系信息 ---
}