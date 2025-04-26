package com.auta.server.api.service.project.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTestDetailResponse {
    private String projectName;

    private String projectAdmin;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEnd;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testExecutionDate;

    private String description;
    private TestCountSummary testSummary;
    private List<PageInfo> pages;


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCountSummary {

        private int totalSuccessTests;
        private int totalFailTests;
        private int interactionSuccessCount;
        private int interactionFailCount;
        private int mappingSuccessCount;
        private int mappingFailCount;
        private int routingSuccessCount;
        private int routingFailCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Long pageId;
        private String pageName;

    }

}
