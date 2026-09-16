package com.ngqabutho.headerscanner.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ScanReportTest {

    @Test
    void highSeverityOkGetsThreePoints() {
        HeaderRule rule = new HeaderRule(
                "test-Header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.OK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        int result = report.score();

        assertEquals(3, result);
    }

    @Test
    void highSeverityWeakGetsOnePoint() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.WEAK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(1, result);
    }

    @Test
    void highSeverityMissingGetsNoPoints() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.MISSING,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(0, result);
    }

    @Test
    void mediumSeverityOkGetsTwoPoints() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.MEDIUM,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.OK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(2, result);
    }

    @Test
    void mediumSeverityWeakGetsOnePoint() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.MEDIUM,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.WEAK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(1, result);
    }

    @Test
    void mediumSeverityMissingGetsNoPoints() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.MEDIUM,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.MISSING,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(0, result);
    }

    @Test
    void lowSeverityOkGetsOnePoint() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.OK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(1, result);
    }

    @Test
    void lowSeverityWeakGetsNoPoints() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.WEAK,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(0, result);
    }

    @Test
    void lowSeverityMissingGetsNoPoints() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        HeaderFinding finding = new HeaderFinding(
                rule,
                Status.MISSING,
                "test-value",
                "test"
        );

        List<HeaderFinding> findings = List.of(finding);

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings

        );

        int result = report.score();

        assertEquals(0, result);
    }

    @Test
    void gradeWhenScoreGreaterThanEleven() {
        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        List<HeaderFinding> findings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HeaderFinding finding = new HeaderFinding(
                    rule,
                    Status.OK,
                    "test-value",
                    "test"
            );
            findings.add(finding);
        }

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        char grade = report.grade();

        assertEquals('A', grade);
    }

}
