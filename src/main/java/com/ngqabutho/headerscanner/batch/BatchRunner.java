package com.ngqabutho.headerscanner.batch;

import com.ngqabutho.headerscanner.model.ScanReport;
import com.ngqabutho.headerscanner.persistence.ScanHistoryRepository;
import com.ngqabutho.headerscanner.persistence.ScanRecord;
import com.ngqabutho.headerscanner.persistence.ScanRecordMapper;
import com.ngqabutho.headerscanner.scan.ScanFailure;
import com.ngqabutho.headerscanner.scan.ScanResult;
import com.ngqabutho.headerscanner.scan.ScanSuccess;
import com.ngqabutho.headerscanner.scan.Scanner;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BatchRunner {

    private final Scanner scanner = new Scanner();
    private final ScanHistoryRepository repository;

    public BatchRunner(ScanHistoryRepository repository){
        this.repository = repository;
    }

    public List<ScanResult> scanAll(List<String> urls, Duration timeout) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<ScanResult>> futures = new ArrayList<>();
            List<ScanResult> results = new ArrayList<>();

            for (String url : urls) {
                Future<ScanResult> future = executor.submit(() -> {
                    try {
                        ScanReport report = scanner.scan(url, timeout);
                        ScanRecord record = ScanRecordMapper.from(report);
                        repository.save(record);
                        return new ScanSuccess(report);
                    } catch (Exception e) {
                        return new ScanFailure(url, e);
                    }
                });

                futures.add(future);
            }

            for (Future<ScanResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            return results;
        }
    }
}
