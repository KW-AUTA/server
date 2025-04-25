package com.auta.server.domain.test;

public enum TestStatus {
    READY("준비"),
    RUNNING("실행 중"),
    PASSED("통과"),
    FAILED("실패");
    
    private final String text;

    TestStatus(String text) {
        this.text = text;
    }
}
