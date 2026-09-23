package com.ngqabutho.headerscanner.batch;

import com.ngqabutho.headerscanner.persistence.ScanHistoryRepository;
import com.ngqabutho.headerscanner.persistence.ScanRecord;

import java.util.ArrayList;
import java.util.List;

public class FakeScanHistoryRepository implements ScanHistoryRepository {

    private final List<ScanRecord> records = new ArrayList<>();

    @Override
    public void save(ScanRecord record) {
        records.add(record);
    }

    @Override
    public List<ScanRecord> findAll() {
        return records;
    }
}