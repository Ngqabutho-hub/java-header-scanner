package com.ngqabutho.headerscanner.render;

import com.ngqabutho.headerscanner.model.ScanReport;
import com.ngqabutho.headerscanner.scan.ScanFailure;
import com.ngqabutho.headerscanner.scan.ScanSuccess;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportRendererTest {

    @Test
    void rendersSuccessfulScan() {

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                List.of()
        );

        ScanSuccess success = new ScanSuccess(report);

        ReportRenderer renderer = new ReportRenderer();

        String output = renderer.render(success);

        assertTrue(output.contains("HTTP Header Scanner"));
        assertTrue(output.contains("https://example.com"));
        assertTrue(output.contains("200"));
        assertTrue(output.contains("Score"));
        assertTrue(output.contains("Grade"));
    }

    @Test
    void renderFailedScan() {

        Exception exception = new Exception("Connection failed");
        ScanFailure failure = new ScanFailure("not-a-real-url", exception);

        ReportRenderer renderer = new ReportRenderer();

        String output = renderer.render(failure);

        assertTrue(output.contains("not-a-real-url"));
        assertTrue(output.contains("Connection failed"));
        assertTrue(output.contains("FAILED"));
    }
}
