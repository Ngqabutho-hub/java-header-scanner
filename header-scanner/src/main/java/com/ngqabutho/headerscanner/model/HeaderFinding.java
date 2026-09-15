package com.ngqabutho.headerscanner.model;

public record HeaderFinding (
        HeaderRule rule,
        Status status,
        String actualValue,
        String note
) {
}
