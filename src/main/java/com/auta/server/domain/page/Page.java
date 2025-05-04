package com.auta.server.domain.page;

import com.auta.server.domain.project.Project;
import com.auta.server.domain.test.Test;
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

//    public List<Test> getRoutingTest() {
//        return tests.stream()
//                .filter(Test::isRoutingTest)
//                .toList();
//    }
//
//    public List<Test> getInterActionTest() {
//        return tests.stream()
//                .filter(test -> test.isInterActionTest() && test.isPassed())
//                .toList();
//    }
//
//    public List<Test> getComponentTest() {
//        return tests.stream()
//                .filter(test -> test.isComponentTest() && test.isPassed())
//                .toList();
//    }
//
//    public List<Test> getPassedTest() {
//        return tests.stream()
//                .filter(test -> test.isComponentTest() && test.isFailed())
//                .toList();
//    }
//
//    public List<Test> getFailedTest() {
//        return tests.stream()
//                .filter(test -> test.isComponentTest() && test.isFailed())
//                .toList();
//    }
}
