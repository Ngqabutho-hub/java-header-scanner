package com.ngqabutho.headerscanner.cli;

import java.time.Duration;
import java.util.List;

public record ScanOptions(
        List<String> urls,
        Duration timeout
) {
}
