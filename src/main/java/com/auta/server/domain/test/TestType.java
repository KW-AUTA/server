package com.auta.server.domain.test;

public enum TestType {
    ROUTING("라우팅 테스트"),
    INTERACTION("인터랙션 테스트"),
    COMPONENT("컴포넌트 테스트");

    private final String text;

    TestType(String text) {
        this.text = text;
    }
}
