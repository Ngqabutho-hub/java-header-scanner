package com.ngqabutho.headerscanner.render;

import com.ngqabutho.headerscanner.model.HeaderFinding;
import com.ngqabutho.headerscanner.scan.ScanFailure;
import com.ngqabutho.headerscanner.scan.ScanResult;
import com.ngqabutho.headerscanner.scan.ScanSuccess;

public class ReportRenderer {

    public String render(ScanResult result) {

        if (result instanceof ScanSuccess success) {
            StringBuilder builder = new StringBuilder();

            builder.append("==============================\n");
            builder.append("HTTP Header Scanner\n");
            builder.append("==============================\n");
            builder.append("URL :\t" + success.report().url()).append("\n");
            builder.append("Final_URL :\t").append(success.report().finalUrl()).append("\n");
            builder.append("Status_code :\t").append(success.report().statusCode()).append("\n");
            builder.append("\n");

            for (HeaderFinding finding : success.report().findings()) {
                builder.append(finding.rule().header()).append("\n");
                builder.append("Status :\t").append(finding.status()).append("\n");
                builder.append("Value :\t").append(finding.actualValue()).append("\n");
            }

            builder.append("\n");
            builder.append("Score :\t").append(success.report().score()).append("\n");
            builder.append("Grade :\t").append(success.report().grade()).append("\n");

            return builder.toString();

        } else if (result instanceof ScanFailure failure) {
            StringBuilder builder = new StringBuilder();

            builder.append("==============================\n");
            builder.append("HTTP Header Scanner\n");
            builder.append("==============================\n");

            builder.append("URL :\t").append(failure.url()).append("\n");
            builder.append("Status :\t" + "FAILED").append("\n");
            builder.append("Error :\t").append(failure.exception().getMessage());

            return builder.toString();
        }

        return "";
    }
}
