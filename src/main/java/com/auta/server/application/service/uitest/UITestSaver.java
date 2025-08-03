package com.auta.server.application.service.uitest;

import com.auta.server.adapter.out.fastapi.response.UITestResponse.Evaluation;
import com.auta.server.adapter.out.s3.S3Adapter;
import com.auta.server.application.port.out.persistence.ui.UITestPort;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.ui.UITest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UITestSaver {

    private final UITestPort uiTestPort;
    private final S3Adapter s3Adapter;

    public void saveAll(Project project, List<Evaluation> evaluations) {
        List<UITest> uiTests = evaluations.stream()
                .map(dto -> UITest.builder()
                        .UIPageUrl(s3Adapter.upload(dto.getHighlightImageUrl()))
                        .UIDescription(dto.getFrameSummary())
                        .project(project)
                        .build())
                .toList();

        uiTestPort.saveAll(uiTests);
    }
}
