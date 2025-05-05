package com.auta.server.adapter.out.persistence.test;

import com.auta.server.adapter.out.persistence.page.PageEntity;
import com.auta.server.adapter.out.persistence.project.ProjectEntity;
import com.auta.server.domain.test.TestStatus;
import com.auta.server.domain.test.TestType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tests")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private PageEntity pageEntity;

    @Enumerated(EnumType.STRING)
    private TestStatus testStatus;

    @Enumerated(EnumType.STRING)
    private TestType testType;

    private String failReason;
    private String triggerSelector;
    private String expectedDestination;
    private String actualDestination;
    
    @Column(name = "`trigger`")
    private String trigger;
    private String expectedAction;
    private String actualAction;

    private String componentName;
}
