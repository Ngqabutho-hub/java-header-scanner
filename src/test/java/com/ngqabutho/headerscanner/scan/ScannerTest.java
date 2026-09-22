package com.ngqabutho.headerscanner.scan;

import com.ngqabutho.headerscanner.model.ScanReport;
import com.ngqabutho.headerscanner.rules.RuleSet;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScannerTest {

    @Test
    void scanReturnsScanReport() throws Exception {
        InetSocketAddress address = new InetSocketAddress(0);
        HttpServer server = HttpServer.create(address,0);

        server.createContext("/", exchange -> {
            String responseBody = "Test response";
            byte[] responseBytes = responseBody.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseBytes);
            }

        });

        try {
            server.start();

            int port = server.getAddress().getPort();
            String url = "http://localhost:" + port + "/";

            Scanner scanner = new Scanner();

            ScanReport report = scanner.scan(url, Duration.ofSeconds(5));

            assertEquals(200, report.statusCode());
            assertEquals(url, report.url());
            assertEquals(url, report.finalUrl());
            assertEquals(RuleSet.RULES.size(), report.findings().size());
        }finally {
            server.stop(0);
        }
    }
}
