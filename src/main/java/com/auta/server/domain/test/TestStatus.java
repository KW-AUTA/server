package com.auta.server.domain.test;

import java.util.Set;

public enum TestStatus {
    READY("준비"),
    RUNNING("실행 중"),
    PASSED("통과"),
    FAILED("실패");

    private final String text;
    private static final Set<TestStatus> COMPLETED_STATUSES = Set.of(PASSED, FAILED);

    TestStatus(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return COMPLETED_STATUSES.contains(this);
    }
}
