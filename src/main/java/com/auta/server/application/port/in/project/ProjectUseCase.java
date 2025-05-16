package com.auta.server.application.port.in.project;

import com.auta.server.domain.project.Project;
import java.time.LocalDate;
import java.util.List;

public interface ProjectUseCase {

    Project createProject(ProjectCommand command, String email, LocalDate registeredDate);

    Project updateProject(ProjectCommand command, Long projectId);

    void deleteProject(Long projectId);

    void executeTest(Long projectId);

    List<Project> findAllByUserId(Long userId);
}
