package com.auta.server.adapter.out.persistence.project;

import com.auta.server.application.port.out.project.ProjectPort;
import com.auta.server.common.exception.BusinessException;
import com.auta.server.common.exception.ErrorCode;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.user.User;
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
        return projectRepository.findById(projectId)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Project update(Project project) {
        ProjectEntity projectEntity = projectRepository.findById(project.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        projectEntity.updateFromDomain(project);
        return ProjectMapper.toDomain(projectEntity);
    }
}
