package com.auta.server.domain.ui;

import com.auta.server.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UITest {
    private Long id;
    private Project project;
    private String UIPageUrl;
    private String UIDescription;
}
