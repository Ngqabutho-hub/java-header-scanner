package com.ngqabutho.headerscanner.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ScanReportTest {

    @Test
    void highSeverityOkGetsThirtyPoints() {
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

        assertEquals(30, result);
    }

    @Test
    void highSeverityWeakGetsHalfPoints() {
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

        assertEquals(15, result);
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
    void mediumSeverityOkGetsFifteenPoints() {
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

        assertEquals(15, result);
    }

    @Test
    void mediumSeverityWeakGetsSevenPoints() {
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

        assertEquals(7, result);
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
    void lowSeverityOkGetsFivePoints() {
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

        assertEquals(5, result);
    }

    @Test
    void lowSeverityWeakGetsTwoPoints() {
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

        assertEquals(2, result);
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
    void gradeWhenScoreIsOneHundred() {

        HeaderRule highRule = new HeaderRule(
                "high-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderRule mediumRule = new HeaderRule(
                "medium-header",
                Severity.MEDIUM,
                "test description",
                "test recommendation",
                null
        );

        HeaderRule lowRule = new HeaderRule(
                "low-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        List<HeaderFinding> findings = List.of(
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(mediumRule, Status.OK, "value", "test"),
                new HeaderFinding(mediumRule, Status.OK, "value", "test"),
                new HeaderFinding(lowRule, Status.OK, "value", "test"),
                new HeaderFinding(lowRule, Status.OK, "value", "test")
        );

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        assertEquals('A', report.grade());
    }

    @Test
    void gradeWhenScoreIsEighty() {

        HeaderRule highRule = new HeaderRule(
                "high-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderRule mediumRule = new HeaderRule(
                "medium-header",
                Severity.MEDIUM,
                "test description",
                "test recommendation",
                null
        );

        HeaderRule lowRule = new HeaderRule(
                "low-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        List<HeaderFinding> findings = List.of(
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(mediumRule, Status.OK, "value", "test"),
                new HeaderFinding(lowRule, Status.OK, "value", "test")
        );

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        assertEquals('B', report.grade());
    }

    @Test
    void gradeWhenScoreIsSeventy() {

        HeaderRule highRule = new HeaderRule(
                "high-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        HeaderRule lowRule = new HeaderRule(
                "low-header",
                Severity.LOW,
                "test description",
                "test recommendation",
                null
        );

        List<HeaderFinding> findings = List.of(
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(highRule, Status.OK, "value", "test"),
                new HeaderFinding(lowRule, Status.OK, "value", "test"),
                new HeaderFinding(lowRule, Status.OK, "value", "test")
        );

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        assertEquals('C', report.grade());
    }

    @Test
    void gradeWhenScoreIsSixty() {

        HeaderRule rule = new HeaderRule(
                "test-header",
                Severity.HIGH,
                "test description",
                "test recommendation",
                null
        );

        List<HeaderFinding> findings = List.of(
                new HeaderFinding(rule, Status.OK, "value", "test"),
                new HeaderFinding(rule, Status.OK, "value", "test")
        );

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        assertEquals('D', report.grade());
    }

    @Test
    void gradeWhenScoreIsZero() {

        List<HeaderFinding> findings = List.of();

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        assertEquals('F', report.grade());
    }
}
