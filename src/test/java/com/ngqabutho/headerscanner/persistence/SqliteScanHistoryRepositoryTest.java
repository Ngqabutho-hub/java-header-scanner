package com.ngqabutho.headerscanner.persistence;

import com.ngqabutho.headerscanner.model.HeaderFinding;
import com.ngqabutho.headerscanner.model.ScanReport;
import  org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqliteScanHistoryRepositoryTest {

    @Test
    void savesAndFindsRecord() {
        SqliteScanHistoryRepository repository =
                new SqliteScanHistoryRepository();

        ScanRecord record = new ScanRecord(
                0,
                "https://example.com",
                "https://example.com",
                200,
                12,
                'A'
        );

        repository.save(record);

        List<ScanRecord> records = repository.findAll();

        assertFalse(records.isEmpty());

        ScanRecord savedRecord = records.get(records.size() - 1);

        assertEquals(record.url(), savedRecord.url());
        assertEquals(record.finalUrl(), savedRecord.finalUrl());
        assertEquals(record.statusCode(), savedRecord.statusCode());
        assertEquals(record.score(), savedRecord.score());
        assertEquals(record.grade(), savedRecord.grade());
    }

    @Test
    void scanReportMapsToScanRecord() {

        List<HeaderFinding> findings = List.of();

        ScanReport report = new ScanReport(
                "https://example.com",
                "https://example.com",
                200,
                findings
        );

        ScanRecord record = ScanRecordMapper.from(report);

        assertEquals(report.url(), record.url());
        assertEquals(report.finalUrl(), record.finalUrl());
        assertEquals(report.statusCode(), record.statusCode());
        assertEquals(report.score(), record.score());
        assertEquals(report.grade(), record.grade());
        assertEquals(0, record.id());
    }
}
