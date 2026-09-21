package com.ngqabutho.headerscanner.scan;

import com.ngqabutho.headerscanner.model.HeaderFinding;
import com.ngqabutho.headerscanner.model.HeaderRule;
import com.ngqabutho.headerscanner.model.ScanReport;
import com.ngqabutho.headerscanner.rules.RuleSet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    public ScanReport scan(String url, Duration timeout) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .timeout(timeout)
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        int statusCode = response.statusCode();
        HttpHeaders headers = response.headers();
        URI finalUri = response.uri();

        Evaluator evaluator = new Evaluator();

        List<HeaderFinding> findings = new ArrayList<>();

        for (HeaderRule rule : RuleSet.RULES) {
            HeaderFinding finding = evaluator.evaluateHeader(rule, headers);
            findings.add(finding);
        }

        ScanReport report = new ScanReport(
                url,
                finalUri.toString(),
                statusCode,
                findings
        );

        return report;
    }
}
