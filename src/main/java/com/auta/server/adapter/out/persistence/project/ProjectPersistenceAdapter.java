package com.auta.server.adapter.out.persistence.project;

import com.auta.server.application.port.out.persistence.project.ProjectPort;
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
    private final ProjectMapper projectMapper;

    @Override
    public Project save(Project project) {
        User user = project.getUser();
        ProjectEntity projectEntity = projectMapper.toEntityWithUser(project, user);
        ProjectEntity savedProject = projectRepository.save(projectEntity);

        return projectMapper.toDomain(savedProject);
    }

    @Override
    public Optional<Project> findById(Long projectId) {
        return projectRepository.findById(projectId)
                .map(projectMapper::toDomain);
    }

    @Override
    public Project update(Project project) {
        ProjectEntity projectEntity = projectRepository.findById(project.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        projectEntity.updateFromDomain(project);
        return projectMapper.toDomain(projectEntity);
    }

    @Override
    public void delete(Project project) {
        ProjectEntity projectEntity = projectRepository.findById(project.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));
        projectRepository.delete(projectEntity);
    }

    @Override
    public List<Project> findByProjectNameWithPaging(String email, String projectName, String sortBy, Long cursor,
                                                     int pageSize) {
        List<ProjectEntity> projectEntities = projectRepository.findByProjectNameWithPaging(email, projectName, sortBy,
                cursor, pageSize);
        return projectEntities.stream().map(projectMapper::toDomain).toList();
    }

    @Override
    public List<Project> findAllByUserId(Long userId) {
        List<ProjectEntity> projectEntities = projectRepository.findAllByUserId(userId);
        return projectEntities.stream().map(projectMapper::toDomain).toList();
    }
}
