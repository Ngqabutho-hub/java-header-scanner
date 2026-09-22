package com.ngqabutho.headerscanner.scan;

import com.ngqabutho.headerscanner.model.ScanReport;

public record ScanSuccess(ScanReport report) implements ScanResult {
}
