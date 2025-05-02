package com.auta.server.adapter.out.persistence.project;

import com.auta.server.adapter.out.persistence.user.UserMapper;
import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectPort {

    private final ProjectRepository projectRepository;

    @Override
    public Project save(Project project) {
        User user = project.getUser();
        ProjectEntity projectEntity = ProjectMapper.toEntityWithUserEntity(project, user);
        ProjectEntity savedProject = projectRepository.save(projectEntity);

        return ProjectMapper.toDomain(savedProject);
    }

    @Override
    public Optional<Project> findById(Long projectId) {
        List<ProjectEntity> projectEntities = projectRepository.findAllById();

        if (userEntities.isEmpty()) {
            return Optional.empty();
        }
        if (userEntities.size() > 1) {
            throw new BusinessException(ErrorCode.DUPLICATED_DB_EMAIL);
        }
        return Optional.of(UserMapper.toDomain(userEntities.get(0)));
    }
}
