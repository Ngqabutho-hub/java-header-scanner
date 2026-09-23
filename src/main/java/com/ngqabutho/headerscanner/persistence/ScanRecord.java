package com.ngqabutho.headerscanner.persistence;

public record ScanRecord(
        long id,
        String url,
        String finalUrl,
        int statusCode,
        int score,
        char grade
) {
}
