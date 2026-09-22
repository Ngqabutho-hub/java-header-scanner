package com.ngqabutho.headerscanner.scan;

import com.ngqabutho.headerscanner.model.HeaderFinding;
import com.ngqabutho.headerscanner.model.HeaderRule;
import com.ngqabutho.headerscanner.model.Severity;

import com.ngqabutho.headerscanner.model.Status;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluatorTest {
    @Test
    void missingHeaderReturnsMissing() {
        HeaderRule rule = new HeaderRule(
                "Test-Header",
                Severity.HIGH,
                "Test-description",
                "Test-recommendation",
                null
                );

        HttpHeaders responseHeaders = HttpHeaders.of(Map.of(), (name, value) -> true);
        Evaluator evaluator = new Evaluator();

        HeaderFinding finding = evaluator.evaluateHeader(rule,responseHeaders);

        assertEquals(Status.MISSING, finding.status());

    }

    @Test
    void presentHeaderWithoutPatternReturnsOk() {
        HeaderRule rule = new HeaderRule(
                "Test-Header",
                Severity.HIGH,
                "Test-description",
                "Test-recommendation",
                null
        );

        HttpHeaders responseHeaders = HttpHeaders.of(Map.of("Test-Header", List.of("some-value")), (name, value) -> true);
        Evaluator evaluator = new Evaluator();

        HeaderFinding finding = evaluator.evaluateHeader(rule, responseHeaders);

        assertEquals(Status.OK, finding.status());

    }

    @Test
    void matchingHeaderPatternReturnsOk() {
        HeaderRule rule = new HeaderRule(
                "X-Content-Type-Options",
                Severity.MEDIUM,
                "Test-description",
                "Test-recommendation",
                Pattern.compile("nosniff")
        );

        HttpHeaders responseHeaders = HttpHeaders.of(Map.of("X-Content-Type-Options", List.of("nosniff")), (name, value) -> true);
        Evaluator evaluator = new Evaluator();

        HeaderFinding finding = evaluator.evaluateHeader(rule, responseHeaders);

        assertEquals(Status.OK, finding.status());
    }

    @Test
    void nonMatchingHeaderPatternReturnsWeak() {
        HeaderRule rule = new HeaderRule(
                "X-Content-Type-Options",
                Severity.MEDIUM,
                "Test-description",
                "Test-recommendation",
                Pattern.compile("nosniff")
        );

        HttpHeaders responseHeaders = HttpHeaders.of(Map.of("X-Content-Type-Options", List.of("something")), (name, value) -> true);
        Evaluator evaluator = new Evaluator();

        HeaderFinding finding = evaluator.evaluateHeader(rule, responseHeaders);

        assertEquals(Status.WEAK, finding.status());

    }
}
