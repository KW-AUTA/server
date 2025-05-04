package com.auta.server.adapter.out.persistence.project;

import com.auta.server.adapter.out.persistence.page.PageMapper;
import com.auta.server.adapter.out.persistence.user.UserMapper;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, PageMapper.class})
public interface ProjectMapper {

    @Mapping(target = "pages", ignore = true)
    @Mapping(source = "projectEntity.userEntity", target = "user")
    Project toDomain(ProjectEntity projectEntity);
    
    @Mapping(target = "pageEntities", ignore = true)
    @Mapping(source = "project.user", target = "userEntity")
    ProjectEntity toEntity(Project project);


    @Mapping(source = "project.id", target = "id")
    @Mapping(source = "user", target = "userEntity")
    @Mapping(target = "pageEntities", ignore = true)
    ProjectEntity toEntityWithUser(Project project, User user);

//    public static Project toDomain(ProjectEntity entity) {
//        return Project.builder()
//                .id(entity.getId())
//                .user(UserMapper.toDomain(entity.getUser()))
//                .figmaUrl(entity.getFigmaUrl())
//                .rootFigmaPage(entity.getRootFigmaPage())
//                .serviceUrl(entity.getServiceUrl())
//                .projectName(entity.getProjectName())
//                .description(entity.getDescription())
//                .projectCreatedDate(entity.getProjectCreatedDate())
//                .projectEnd(entity.getProjectEnd())
//                .projectStatus(entity.getProjectStatus())
//                .build();
//    }
//
//    public static ProjectEntity toEntityWithUserEntity(Project project, User user
//    ) {
//        return ProjectEntity.builder()
//                .id(project.getId())
//                .user(UserMapper.toEntity(user))
//                .figmaUrl(project.getFigmaUrl())
//                .rootFigmaPage(project.getRootFigmaPage())
//                .serviceUrl(project.getServiceUrl())
//                .projectName(project.getProjectName())
//                .description(project.getDescription())
//                .projectCreatedDate(project.getProjectCreatedDate())
//                .projectEnd(project.getProjectEnd())
//                .projectStatus(project.getProjectStatus())
//                .build();
//    }
}
