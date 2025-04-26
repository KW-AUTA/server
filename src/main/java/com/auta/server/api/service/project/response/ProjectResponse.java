package com.auta.server.api.service.project.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long projectId;
    private String projectName;
    private String administrator;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String rootFigmaPage;
}
