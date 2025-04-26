package com.auta.server.domain.project;

public enum ProjectStatus {
    NOT_STARTED("진행 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("진행 완료");

    private final String text;
    
    ProjectStatus(String text) {
        this.text = text;
    }
}
