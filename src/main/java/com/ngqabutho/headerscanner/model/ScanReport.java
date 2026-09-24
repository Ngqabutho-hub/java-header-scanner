package com.ngqabutho.headerscanner.model;

import java.util.List;

public record ScanReport(
        String url,
        String finalUrl,
        int statusCode,
        List<HeaderFinding> findings
) {

    public int score(){
        int score = 0;

        for (HeaderFinding finding : findings) {

            Severity severity = finding.rule().severity();
            Status status = finding.status();

            int weight;

            if (severity == Severity.HIGH) {
                weight = 30;
            } else if (severity == Severity.MEDIUM) {
                weight = 15;
            } else {
                weight = 5;
            }

            if (status == Status.OK) {
                score += weight;
            } else if (status == Status.WEAK) {
                score += weight / 2;
            }
        }

        return score;
    }
    
    public char grade() {
        int score = score();

        if (score >= 90) {
            return 'A';
        } else if (score >= 80) {
            return 'B';
        } else if (score >= 70) {
            return 'C';
        } else if (score >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }
}
