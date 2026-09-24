package com.ngqabutho.headerscanner.cli;

import java.time.Duration;
import java.util.List;

public class ArgParser {

    public ScanOptions parse(String[] args) {

        if (args.length == 0) {
            throw new IllegalArgumentException("At least one URL is required");
        }

        List<String> urls = List.of(args);

        Duration timeout = Duration.ofSeconds(5);

        return new ScanOptions(urls, timeout);
    }
}
