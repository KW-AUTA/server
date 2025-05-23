package com.auta.server.adapter.in.project.request;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {
    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    private String projectName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedTestExecution;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;

    private String description;

    @NotBlank(message = "피그마 url은 필수입니다.")
    private String figmaUrl;

    @NotBlank(message = "서비스 url은 필수입니다.")
    private String serviceUrl;

    @NotBlank(message = "피그마 시작 페이지 이름은 필수입니다.")
    private String rootFigmaPage;

    public ProjectCommand toCommand() {
        return ProjectCommand.builder()
                .projectName(projectName)
                .expectedTestExecution(expectedTestExecution)
                .projectEnd(projectEnd)
                .description(description)
                .figmaUrl(figmaUrl)
                .serviceUrl(serviceUrl)
                .rootFigmaPage(rootFigmaPage)
                .build();
    }
}
