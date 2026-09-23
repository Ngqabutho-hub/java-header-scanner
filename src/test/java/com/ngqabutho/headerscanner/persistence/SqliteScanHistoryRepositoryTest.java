package com.ngqabutho.headerscanner.persistence;

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
}
