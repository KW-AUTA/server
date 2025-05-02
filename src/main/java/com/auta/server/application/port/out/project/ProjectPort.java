package com.auta.server.application.port.out.project;

import com.auta.server.domain.project.Project;
import java.util.Optional;

public interface ProjectPort {
    Project save(Project project);

    Optional<Project> findById(Long projectId);
}
