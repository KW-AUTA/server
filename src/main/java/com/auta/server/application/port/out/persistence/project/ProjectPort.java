package com.auta.server.application.port.out.persistence.project;

import com.auta.server.domain.project.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectPort {
    Project save(Project project);

    Optional<Project> findById(Long projectId);

    Project update(Project project);

    void delete(Project project);

    List<Project> findByProjectNameWithPaging(String email, String projectName, String sortBy, Long cursor,
                                              int pageSize);

    List<Project> findAllByUserId(Long userId);
}
