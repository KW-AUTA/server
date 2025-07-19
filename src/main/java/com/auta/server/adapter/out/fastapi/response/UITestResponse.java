package com.auta.server.adapter.out.fastapi.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UITestResponse {
    private int usabilityScore;
    private List<Evaluation> evaluations;

    @Getter
    @Builder
    public static class Evaluation {
        private String frameName;
        private List<ProblemComponent> problemComponents;
        private String highlightImageUrl;
    }

    @Getter
    @Builder
    public static class ProblemComponent {
        private String id;
        private String name;
        private String issueType;
        private String reason;
    }
}



