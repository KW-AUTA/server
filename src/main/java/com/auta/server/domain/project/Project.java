package com.auta.server.domain.project;

import com.auta.server.application.port.in.project.ProjectCommand;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private User user;
    private List<Page> pages;
    private String figmaUrl;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private LocalDate projectCreatedDate;
    private LocalDate projectEnd;
    private ProjectStatus projectStatus;
    private LocalDateTime testExecuteTime;
    private Integer testRate;

    public void update(ProjectCommand command) {
        this.projectName = command.getProjectName();
        this.projectEnd = command.getProjectEnd();
        this.description = command.getDescription();
        this.figmaUrl = command.getFigmaUrl();
        this.serviceUrl = command.getServiceUrl();
        this.rootFigmaPage = command.getRootFigmaPage();
    }

    public int getTotalRoutingTest() {
        return getTotalTestBy(Page::getTotalRouting);
    }

    public int getTotalInteractionTest() {
        return getTotalTestBy(Page::getTotalInteraction);
    }

    public int getTotalMappingTest() {
        return getTotalTestBy(Page::getTotalMapping);
    }
    
    public void addPages(List<Page> pages) {
        this.pages = pages;
    }

    private int getTotalTestBy(Function<Page, Long> extractor) {
        return pages == null ? 0 :
                pages.stream()
                        .mapToInt(p -> extractor.apply(p).intValue())
                        .sum();
    }
}
