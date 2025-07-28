package com.auta.server.adapter.out.persistence.project;

import com.auta.server.adapter.out.BaseEntity;
import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.ui.UITestEntity;
import com.auta.server.adapter.out.persistence.user.UserEntity;
import com.auta.server.domain.project.Project;
import com.auta.server.domain.project.ProjectStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PageEntity> pageEntities;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UITestEntity> uiTestEntities;

    private String figmaUrl;
    private String figmaJson;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private String fileName;
    private LocalDate expectedTestExecution;
    private LocalDate projectCreatedDate;
    private LocalDate projectEnd;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private LocalDateTime testExecuteTime;
    private Integer testRate;
    private Integer score;

    public void updateFromDomain(Project project) {
        this.projectName = project.getProjectName();
        this.expectedTestExecution = project.getExpectedTestExecution();
        this.projectEnd = project.getProjectEnd();
        this.description = project.getDescription();
        this.fileName = project.getFileName();
        this.figmaUrl = project.getFigmaUrl();
        this.serviceUrl = project.getServiceUrl();
        this.rootFigmaPage = project.getRootFigmaPage();
        this.score = project.getScore();
        this.testRate = project.getTestRate();
    }

    public void updateProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }
}
