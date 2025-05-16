package com.auta.server.domain.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;

class TestCountSummaryTest {

    @DisplayName("전체 테스트 중에 성공한 테스트 수를 반환 한다.")
    @org.junit.jupiter.api.Test
    void totalPassed() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED),
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED),
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)

        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int totalRoutingTest = summary.totalPassed();

        //then
        assertThat(totalRoutingTest).isEqualTo(3);
    }

    @DisplayName("전체 테스트 중에 실패한 테스트 수를 반환 한다.")
    @org.junit.jupiter.api.Test
    void totalFailed() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED),
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED),
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)

        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int totalRoutingTest = summary.totalFail();

        //then
        assertThat(totalRoutingTest).isEqualTo(3);
    }

    @DisplayName("라우팅 테스트 3개 중 1개가 성공 1개가 실패, 1개가 준비중 이면 total은 2를 반환한다.")
    @org.junit.jupiter.api.Test
    void typeTotal() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)
        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int totalRoutingTest = summary.typeTotal(TestType.ROUTING);

        //then
        assertThat(totalRoutingTest).isEqualTo(2);
    }

    @DisplayName("라우팅 테스트 3개 중 1개가 성공 1개가 실패, 1개가 준비중 이면 passed는 1을 반환한다.")
    @org.junit.jupiter.api.Test
    void typePassed() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.PASSED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)
        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int success = summary.typePassed(TestType.ROUTING);

        //then
        assertThat(success).isEqualTo(1);
    }

    @DisplayName("라우팅 테스트 3개 중 1개가 성공 1개가 실패, 1개가 준비중 이면 typeFaild 2을 반환한다.")
    @org.junit.jupiter.api.Test
    void typeFailed() {
        //given
        List<Test> tests = List.of(
                makeTest(TestType.ROUTING, TestStatus.FAILED),
                makeTest(TestType.ROUTING, TestStatus.READY),
                makeTest(TestType.ROUTING, TestStatus.FAILED)
        );
        TestCountSummary summary = TestCountSummary.from(tests);

        //when
        int failed = summary.typeFailed(TestType.ROUTING);

        //then
        assertThat(failed).isEqualTo(2);
    }

    private Test makeTest(TestType type, TestStatus status) {
        return Test.builder()
                .testType(type)
                .testStatus(status)
                .build();
    }

}