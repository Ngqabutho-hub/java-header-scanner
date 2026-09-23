package com.ngqabutho.headerscanner.persistence;

import java.util.List;

public interface ScanHistoryRepository {

    void save(ScanRecord record);

    List<ScanRecord> findAll();
}
