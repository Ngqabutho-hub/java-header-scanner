package com.ngqabutho.headerscanner.model;

import java.util.regex.Pattern;

public record HeaderRule(
        String header,
        Severity severity,
        String description,
        String recommendation,
        Pattern mustMatch
) {
}
