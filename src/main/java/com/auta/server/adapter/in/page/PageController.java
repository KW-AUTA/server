package com.auta.server.adapter.in.page;

import com.auta.server.adapter.in.ApiResponse;
import com.auta.server.adapter.in.page.response.PageTestResponse;
import com.auta.server.application.port.in.page.PageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageController {

    private final PageUseCase pageUseCase;

    @GetMapping("/api/v1/pages/{pageId}")
    public ApiResponse<PageTestResponse> getPageTestDetail(@PathVariable Long pageId) {
        return ApiResponse.ok("프로젝트 페이지 테스트 세부 조회가 완료되었습니다.",
                PageTestResponse.from(pageUseCase.getPageTestDetail(pageId)));
    }
}
