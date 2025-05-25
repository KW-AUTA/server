package com.auta.server.domain.page;

import com.auta.server.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Page {
    private Long id;
    private Project project;
    private String pageName;
    private String pageBaseUrl;

    @Builder
    private Page(Project project, String pageName, String pageBaseUrl) {
        this.project = project;
        this.pageName = pageName;
        this.pageBaseUrl = pageBaseUrl;
    }

    public static Page of(Project project, String pageName, String pageBaseUrl) {
        return Page.builder()
                .project(project)
                .pageName(pageName)
                .pageBaseUrl(pageBaseUrl)
                .build();
    }
}
