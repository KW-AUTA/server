package com.auta.server.api.controller.page;

import com.auta.server.api.ApiResponse;
import com.auta.server.api.service.page.PageService;
import com.auta.server.api.service.page.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/api/v1/pages/{projectId}")
    public ApiResponse<PageResponse> getPages(@PathVariable Long projectId) {
        return ApiResponse.ok("페이지 조회가 완료되었습니다.", pageService.getPagesBy(projectId));
    }
}