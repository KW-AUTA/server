package com.auta.server.domain.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;

class TestCountSummaryTest {

    @DisplayName("라우팅 테스트 3개 중 1개가 성공 1개가 실패, 1개가 준비중 이면 total은 2를 반환한다.")
    @org.junit.jupiter.api.Test
    void total() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)
        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int totalRoutingTest = summary.total(TestType.ROUTING);

        //then
        assertThat(totalRoutingTest).isEqualTo(2);
    }

    @DisplayName("라우팅 테스트 3개 중 1개가 성공 1개가 실패, 1개가 준비중 이면 passed는 1을 반환한다.")
    @org.junit.jupiter.api.Test
    void passed() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)
        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int success = summary.passed(TestType.ROUTING);

        //then
        assertThat(success).isEqualTo(1);
    }

    private Test makeTest(TestType type, TestStatus status) {
        return Test.builder()
                .testType(type)
                .testStatus(status)
                .build();
    }

}