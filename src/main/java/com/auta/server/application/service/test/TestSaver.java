package com.auta.server.application.service.test;

import com.auta.server.application.port.out.persistence.page.PagePort;
import com.auta.server.application.port.out.persistence.test.TestPort;
import com.auta.server.domain.page.Page;
import com.auta.server.domain.test.Test;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestSaver {
    private final PagePort pagePort;
    private final TestPort testPort;

    public void saveAll(List<Page> pages, List<Test> tests) {
        List<Page> savedPages = pagePort.saveAll(pages);
        reassignPages(tests, savedPages);
        testPort.saveAll(tests);
    }

    private void reassignPages(List<Test> tests, List<Page> savedPages) {
        Map<String, Page> pageMap = savedPages.stream()
                .collect(Collectors.toMap(Page::getPageName, Function.identity()));
        for (Test test : tests) {
            Page original = test.getPage();
            Page saved = pageMap.get(original.getPageName());
            test.reassignPage(saved);
        }
    }
}
