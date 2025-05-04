package com.auta.server.domain.page;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
import com.auta.server.domain.test.TestType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page {
    private Long id;
    private Project project;
    private List<Test> tests;
    private String pageName;
    private String pageBaseUrl;

    public long getTotalRouting() {
        return getTotalByType(TestType.ROUTING);
    }

    public long getTotalInteraction() {
        return getTotalByType(TestType.INTERACTION);
    }

    public long getTotalMapping() {
        return getTotalByType(TestType.COMPONENT);
    }

    public void addTests(List<Test> tests) {
        this.tests = tests;
    }

    private long getTotalByType(TestType type) {
        if (tests == null) {
            return 0;
        }
        return tests.stream()
                .filter(t -> t.getTestType() == type)
                .count();
    }
}
