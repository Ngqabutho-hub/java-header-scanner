package com.ngqabutho.headerscanner;

import com.ngqabutho.headerscanner.batch.BatchRunner;
import com.ngqabutho.headerscanner.cli.ArgParser;
import com.ngqabutho.headerscanner.cli.ScanOptions;
import com.ngqabutho.headerscanner.persistence.ScanHistoryRepository;
import com.ngqabutho.headerscanner.persistence.SqliteScanHistoryRepository;
import com.ngqabutho.headerscanner.render.ReportRenderer;
import com.ngqabutho.headerscanner.scan.ScanResult;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ArgParser parser = new ArgParser();
        ScanOptions options = parser.parse(args);

        ScanHistoryRepository repository = new SqliteScanHistoryRepository();
        BatchRunner batchRunner = new BatchRunner(repository);

        List<ScanResult> results = batchRunner.scanAll(options.urls(), options.timeout());

        ReportRenderer renderer = new ReportRenderer();

        for (ScanResult result : results) {
            System.out.println(renderer.render(result));
        }
    }
}
