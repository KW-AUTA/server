package com.auta.server.adapter.out.persistence.project;

import com.auta.server.adapter.out.BaseEntity;
import com.auta.server.adapter.out.persistence.page.PageEntity;
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

    private String figmaUrl;
    private String rootFigmaPage;
    private String serviceUrl;
    private String projectName;
    private String description;
    private LocalDate projectCreatedDate;
    private LocalDate projectEnd;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private Integer testExecuteTime;
    private Integer testRate;

    public void updateFromDomain(Project project) {
        this.projectName = project.getProjectName();
        this.projectEnd = project.getProjectEnd();
        this.description = project.getDescription();
        this.figmaUrl = project.getFigmaUrl();
        this.serviceUrl = project.getServiceUrl();
        this.rootFigmaPage = project.getRootFigmaPage();
    }
}
