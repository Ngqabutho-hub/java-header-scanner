package com.ngqabutho.headerscanner.persistence;

import com.ngqabutho.headerscanner.model.ScanReport;

public class ScanRecordMapper {

    public static ScanRecord from(ScanReport report) {

        long id = 0;
        String url = report.url();
        String finalUrl = report.finalUrl();
        int statusCode = report.statusCode();
        int score = report.score();
        char grade = report.grade();

        return new ScanRecord(id, url, finalUrl, statusCode, score, grade);
    }
}
