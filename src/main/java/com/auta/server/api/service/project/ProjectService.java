package com.auta.server.api.service.project;

import com.auta.server.api.service.project.request.ProjectServiceRequest;
import com.auta.server.api.service.project.response.ProjectResponse;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    public ProjectResponse createProject(ProjectServiceRequest request, String email) {
        return null;
    }

    public ProjectResponse updateProject(ProjectServiceRequest serviceEntity, Long projectId) {
        return null;
    }

    public void deleteProject(Long projectId) {
    }
}
