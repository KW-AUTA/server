package com.auta.server.adapter.in.project.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class ProjectDetailResponse {
    private String projectName;
    private String projectAdmin;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectCreatedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testExecutionDate;

    private String figmaRootPage;
    private String description;
    private String figmaUrl;
    private String serviceUrl;
    private String reportSummary;

    private TestSummary testSummary;

    private List<PageInfo> pages;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestSummary {
        private int totalRoutingTest;
        private int totalInteractionTest;
        private int totalMappingTest;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private String pageName;
        private String pageUrl;
        private String pageImageUrl;
    }
}
