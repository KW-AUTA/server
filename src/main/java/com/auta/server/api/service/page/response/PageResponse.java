package com.auta.server.api.service.page.response;

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
public class PageResponse {
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate testExecutionDate;
    private List<PageInfo> pages;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private Long pageId;
        private String pageName;
    }
}
