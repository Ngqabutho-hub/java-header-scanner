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

        for (HeaderFinding finding: findings){
            Status status = finding.status();
            Severity severity = finding.rule().severity();

            if (severity.equals(Severity.HIGH)){
                if (status.equals(Status.OK)){
                    score += 3;
                } else if (status.equals(Status.WEAK)){
                    score += 1;
                }
            } else if (severity.equals(Severity.MEDIUM)){
                if (status.equals(Status.OK)){
                    score += 2;
                } else if (status.equals(Status.WEAK)){
                    score += 1;
                }
            }else{
                if (status.equals(Status.OK)){
                    score += 1;
                }
            }
        }
        return score;
    }
    
    public char grade() {
        int score = score();

        if (score >= 12){
            return 'A';
        } else if (score >= 9){
            return 'B';
        } else if (score >= 6){
            return 'C';
        } else if (score >= 3){
            return 'D';
        } else {
            return 'F';
        }
    }
}
