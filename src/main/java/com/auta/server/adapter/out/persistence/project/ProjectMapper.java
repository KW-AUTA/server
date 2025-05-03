package com.auta.server.adapter.out.persistence.project;

import com.auta.server.adapter.out.persistence.user.UserMapper;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ProjectMapper {
    Project toDomain(ProjectEntity entity);

    @Mapping(source = "domain.id", target = "id")
    @Mapping(source = "user", target = "user")
    ProjectEntity toEntityWithUser(Project domain, User user);

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
