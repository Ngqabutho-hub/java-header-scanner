package com.ngqabutho.headerscanner.batch;

import com.ngqabutho.headerscanner.scan.ScanFailure;
import com.ngqabutho.headerscanner.scan.ScanResult;
import com.ngqabutho.headerscanner.scan.ScanSuccess;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BatchRunnerTest {

    @Test
    void scansMultipleValidUrls() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);

        server.createContext("/", exchange -> {
           exchange.sendResponseHeaders(200, 0);
           exchange.getResponseBody().close();
        });

        try {
            server.start();

            int port = server.getAddress().getPort();

            List<String> urls = List.of(
                    "http://localhost:" + port + "/",
                    "http://localhost:" + port + "/"
            );

            FakeScanHistoryRepository repository = new FakeScanHistoryRepository();
            BatchRunner batchRunner = new BatchRunner(repository);
            List<ScanResult> results = batchRunner.scanAll(urls, Duration.ofSeconds(5));

            assertEquals(2, results.size());
            assertInstanceOf(ScanSuccess.class, results.get(0));
            assertInstanceOf(ScanSuccess.class, results.get(1));
            assertEquals(2, repository.findAll().size());

        }finally {
            server.stop(0);
        }
    }

    @Test
    void scansValidAndInvalidUrls() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);

        server.createContext("/", exchange -> {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        });

        try {
            server.start();

            int port = server.getAddress().getPort();

            List<String> urls = List.of(
                    "http://localhost:" + port + "/",
                    "not-a-valid-url"
            );

            FakeScanHistoryRepository repository = new FakeScanHistoryRepository();
            BatchRunner batchRunner = new BatchRunner(repository);
            List<ScanResult> results = batchRunner.scanAll(urls, Duration.ofSeconds(5));

            assertEquals(2, results.size());
            assertInstanceOf(ScanSuccess.class, results.get(0));
            assertInstanceOf(ScanFailure.class, results.get(1));
            assertEquals(1, repository.findAll().size());

        }finally {
            server.stop(0);
        }
    }

}
