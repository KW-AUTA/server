package com.auta.server.domain.project;

import com.auta.server.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private User user;
    private List<Long> testIds;
    private List<Long> pageIds;
    private String figmaUrl;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private LocalDate projectCreatedDate;
    private LocalDate projectEnd;
    private ProjectStatus projectStatus;
}
