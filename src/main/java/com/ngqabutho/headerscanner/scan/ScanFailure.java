package com.ngqabutho.headerscanner.scan;

public record ScanFailure(String url, Exception exception) implements ScanResult {
}
