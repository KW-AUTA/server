package com.auta.server.adapter.out.persistence.projectprogress;

import com.auta.server.adapter.out.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_test_progress")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTestProgressEntity extends BaseEntity {
    @Id
    private Long projectId;

    private boolean isUiDone;

    private boolean isTestDone;

    public ProjectTestProgressEntity(Long projectId) {
        this.projectId = projectId;
        isUiDone = false;
        isTestDone = false;
    }

    public void updateTest(boolean isAsyncDone) {
        this.isTestDone = isAsyncDone;
    }

    public void updateUITest(boolean isUiDone) {
        this.isUiDone = isUiDone;
    }

    public boolean isUiDone() {
        return isUiDone;
    }

    public boolean isTestDone() {
        return isTestDone;
    }
}

