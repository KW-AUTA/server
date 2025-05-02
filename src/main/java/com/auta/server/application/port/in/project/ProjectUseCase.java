package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.Project;
import java.time.LocalDate;

public interface ProjectUseCase {

    Project createProject(ProjectCommand command, String email, LocalDate registeredDate);

    Project updateProject(ProjectCommand command, Long projectId);

    void deleteProject(Long projectId);
}
