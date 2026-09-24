# HTTP Header Scanner

A Java-based command-line tool for scanning websites for important HTTP security headers, evaluating their configuration, and generating a security score and grade.

This project is a Java port of an existing Python HTTP security-header scanner. It was developed as a **solo WeThinkCode elective project** with additional functionality implemented beyond the original scanner, including concurrent multi-URL scanning using **Java virtual threads** and **SQLite-based scan history persistence**.

---

## Table of Contents

* [Overview](#overview)
* [Project Goals](#project-goals)
* [Features](#features)
* [Security Headers Checked](#security-headers-checked)
* [Scoring System](#scoring-system)
* [Architecture](#architecture)
* [Project Structure](#project-structure)
* [How the Scanner Works](#how-the-scanner-works)
* [Concurrency](#concurrency)
* [Persistence](#persistence)
* [Technologies](#technologies)
* [Requirements](#requirements)
* [Installation](#installation)
* [Running the Application](#running-the-application)
* [Running Multiple Scans](#running-multiple-scans)
* [Example Output](#example-output)
* [Running Tests](#running-tests)
* [Testing Strategy](#testing-strategy)
* [Error Handling](#error-handling)
* [Design Decisions](#design-decisions)
* [Learning Outcomes](#learning-outcomes)
* [Future Improvements](#future-improvements)
* [Author](#author)

---

# Overview

HTTP security headers are response headers that allow a website to communicate security-related policies to a browser.

Incorrect or missing headers can increase exposure to attacks such as:

* Cross-site scripting (XSS)
* Clickjacking
* MIME-type sniffing
* SSL-stripping
* Unnecessary browser feature access
* Unintended information leakage through referrers

The **HTTP Header Scanner** sends an HTTP request to a target URL, retrieves the response headers, and evaluates a predefined set of security headers.

Each header is classified as:

* `OK` - The header is present and satisfies the configured rule.
* `WEAK` - The header is present but does not satisfy its required configuration.
* `MISSING` - The header is not present.

The scanner then calculates a score and assigns an A-F grade.

---

# Project Goals

The main goal of this project was to gain practical experience implementing a real-world Java application while translating an existing Python implementation into Java.

The project focused on:

1. Learning and applying Java fundamentals.
2. Applying object-oriented design.
3. Using Java records and enums to model application data.
4. Working with Java's HTTP Client API.
5. Using regular expressions for security-header validation.
6. Building a Maven project.
7. Writing automated tests using JUnit.
8. Implementing concurrent processing with Java virtual threads.
9. Persisting scan results using SQLite.
10. Separating responsibilities across different packages and classes.
11. Building a command-line application that can be executed from a terminal.

---

# Features

## HTTP Security Header Scanning

The scanner checks a predefined collection of security-related HTTP headers.

## Header Evaluation

Headers are evaluated according to their individual rules.

Rules can specify:

* Header name
* Severity
* Description
* Recommendation
* Optional regular-expression validation

## Security Scoring

Each header contributes points based on its severity and status.

The scanner produces a score out of 100.

## A-F Grading

The score is converted into a letter grade:

|    Score | Grade |
| -------: | :---: |
|   90-100 |   A   |
|    80-89 |   B   |
|    70-79 |   C   |
|    60-69 |   D   |
| Below 60 |   F   |

## Multiple URL Scanning

Multiple URLs can be supplied to the application.

Each URL is scanned independently.

## Virtual-Thread Concurrency

Multiple scans are submitted to an `ExecutorService` using Java virtual threads.

This allows network-bound scans to execute concurrently without creating a traditional platform thread for every task.

## SQLite Scan History

Successful scans are persisted to a local SQLite database.

The database stores information such as:

* Original URL
* Final URL
* HTTP status code
* Score
* Grade

## Automated Testing

The project contains unit and integration-style tests covering:

* Header evaluation
* Scoring
* Grading
* HTTP scanning
* Batch scanning
* Failure handling
* Report rendering
* SQLite persistence

---

# Security Headers Checked

The scanner currently checks six headers.

| Header                      | Severity | Purpose                                           |
| --------------------------- | -------- | ------------------------------------------------- |
| `Strict-Transport-Security` | HIGH     | Enforces HTTPS connections                        |
| `Content-Security-Policy`   | HIGH     | Controls which resources browsers may load        |
| `X-Content-Type-Options`    | MEDIUM   | Helps prevent MIME-type sniffing                  |
| `X-Frame-Options`           | MEDIUM   | Helps prevent clickjacking                        |
| `Referrer-Policy`           | LOW      | Controls referrer information sent to other sites |
| `Permissions-Policy`        | LOW      | Controls access to browser features               |

Some headers only need to be present, while others require their value to match a specific pattern.

For example, `Strict-Transport-Security` is checked for a valid positive `max-age` value.

---

# Scoring System

The scanner uses weighted scoring based on header severity.

### HIGH

Maximum: **30 points**

* `OK` = 30
* `WEAK` = 15
* `MISSING` = 0

### MEDIUM

Maximum: **15 points**

* `OK` = 15
* `WEAK` = 7
* `MISSING` = 0

### LOW

Maximum: **5 points**

* `OK` = 5
* `WEAK` = 2
* `MISSING` = 0

With six configured rules, the maximum score is:

```text
30 + 30 + 15 + 15 + 5 + 5 = 100
```

The resulting score is then converted into an A-F grade.

---

# Architecture

The project is organised around separation of responsibilities.

```text
                        ┌─────────────┐
                        │     CLI     │
                        └──────┬──────┘
                               │
                               ▼
                        ┌─────────────┐
                        │ BatchRunner │
                        └──────┬──────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
               ┌─────────┐         ┌────────────┐
               │ Scanner │         │ Persistence│
               └────┬────┘         └────────────┘
                    │
                    ▼
               ┌───────────┐
               │ Evaluator │
               └─────┬─────┘
                     │
                     ▼
                ┌───────────┐
                │ ScanReport│
                └─────┬─────┘
                      │
             ┌────────┴────────┐
             ▼                 ▼
       ┌────────────┐   ┌───────────────┐
       │ Renderer   │   │ SQLite Mapper │
       └────────────┘   └───────────────┘
```

### Main flow

```text
User input
    ↓
ArgParser
    ↓
ScanOptions
    ↓
BatchRunner
    ↓
Scanner
    ↓
HTTP request
    ↓
Evaluator
    ↓
HeaderFinding
    ↓
ScanReport
    ↓
Score + Grade
    ↓
ScanRecord
    ↓
SQLite
    ↓
ReportRenderer
    ↓
Terminal output
```

---

# Project Structure

```text
header-scanner/
│
├── pom.xml
├── .gitignore
│
├── data/
│   └── scans.db
│
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── ngqabutho/
    │               └── headerscanner/
    │
    │                   ├── Main.java
    │                   │
    │                   ├── batch/
    │                   │   └── BatchRunner.java
    │                   │
    │                   ├── cli/
    │                   │   ├── ArgParser.java
    │                   │   └── ScanOptions.java
    │                   │
    │                   ├── model/
    │                   │   ├── HeaderFinding.java
    │                   │   ├── HeaderRule.java
    │                   │   ├── ScanReport.java
    │                   │   ├── Severity.java
    │                   │   └── Status.java
    │                   │
    │                   ├── persistence/
    │                   │   ├── ScanHistoryRepository.java
    │                   │   ├── ScanRecord.java
    │                   │   ├── ScanRecordMapper.java
    │                   │   └── SqliteScanHistoryRepository.java
    │                   │
    │                   ├── render/
    │                   │   └── ReportRenderer.java
    │                   │
    │                   ├── rules/
    │                   │   └── RuleSet.java
    │                   │
    │                   └── scan/
    │                       ├── Evaluator.java
    │                       ├── ScanFailure.java
    │                       ├── ScanResult.java
    │                       ├── ScanSuccess.java
    │                       └── Scanner.java
    │
    └── test/
        └── java/
            └── com/
                └── ngqabutho/
                    └── headerscanner/
                        ├── batch/
                        ├── model/
                        ├── persistence/
                        ├── render/
                        └── scan/
```

The local SQLite database and Maven's generated `target/` directory are ignored by Git.

---

# How the Scanner Works

## 1. Parse the command-line arguments

`ArgParser` receives the arguments supplied to the application and converts them into a `ScanOptions` object.

`ScanOptions` contains:

* URLs
* Request timeout

A default timeout is currently used for requests.

---

## 2. Start the batch

`BatchRunner` receives the URLs and creates a virtual-thread executor.

Each URL becomes a separate scanning task.

Conceptually:

```text
URL 1 ──→ Virtual Thread
URL 2 ──→ Virtual Thread
URL 3 ──→ Virtual Thread
URL 4 ──→ Virtual Thread
```

The tasks can execute concurrently.

---

## 3. Send the HTTP request

`Scanner` uses Java's built-in HTTP Client API.

The scanner:

1. Creates a `URI`.
2. Builds an HTTP request.
3. Sends the request.
4. Retrieves the response.
5. Extracts the status code.
6. Extracts the response headers.
7. Determines the final URI after redirects.

---

## 4. Evaluate the headers

`Evaluator` takes:

```text
HeaderRule + HttpHeaders
```

and produces:

```text
HeaderFinding
```

For example:

```text
Header:
Strict-Transport-Security

Response:
Strict-Transport-Security: max-age=31536000

Result:
OK
```

If the header is absent:

```text
Result:
MISSING
```

If the header exists but fails its configured pattern:

```text
Result:
WEAK
```

---

# Data Model

The project uses Java records for immutable data structures.

## HeaderRule

Represents the rules used to evaluate a security header.

```text
HeaderRule
├── header
├── severity
├── description
├── recommendation
└── mustMatch
```

`mustMatch` is optional because not every header requires a specific value pattern.

---

## HeaderFinding

Represents the result of evaluating one header.

```text
HeaderFinding
├── rule
├── status
├── actualValue
└── note
```

---

## ScanReport

Represents the complete result of scanning one URL.

```text
ScanReport
├── url
├── finalUrl
├── statusCode
└── findings
```

The report also provides methods for calculating the score and grade.

---

## ScanResult

The batch scanner uses a result abstraction that can represent either a successful scan or a failed scan.

```text
ScanResult
├── ScanSuccess
└── ScanFailure
```

This allows one failed URL to be represented without necessarily stopping the processing of other URLs.

---

# Concurrency

One of the extensions added to the original scanner is concurrent scanning.

The project uses:

```java
Executors.newVirtualThreadPerTaskExecutor()
```

Java virtual threads are particularly suitable for this type of application because HTTP scanning spends much of its time waiting for network operations.

Instead of scanning:

```text
URL 1 → wait → finish
URL 2 → wait → finish
URL 3 → wait → finish
```

the batch runner can submit the scans together:

```text
URL 1 → ───────────────→ result
URL 2 → ─────────→ result
URL 3 → ─────────────────→ result
```

The `Future<ScanResult>` objects allow the application to retrieve the results after submitting the tasks.

The results are collected in the same order as the URLs were submitted.

---

# Persistence

Successful scans are saved to SQLite.

The persistence layer uses a repository abstraction:

```text
ScanHistoryRepository
        │
        ▼
SqliteScanHistoryRepository
```

This separates the rest of the application from the details of SQLite.

The scanner produces a `ScanReport`.

The mapper converts it into a persistence-oriented `ScanRecord`.

```text
ScanReport
    ↓
ScanRecordMapper
    ↓
ScanRecord
    ↓
SQLite
```

The database is stored locally and is intentionally excluded from version control.

---

# Technologies

## Java

The project targets **Java 21**.

Java 21 was selected because the project uses modern Java features including virtual threads and records.

## Maven

Maven is used for:

* Project management
* Dependency management
* Compilation
* Testing
* Running the application

## JUnit 5

JUnit is used for automated testing.

## SQLite

SQLite is used for local scan-history persistence.

The project uses the Xerial SQLite JDBC driver.

## Java HTTP Client

The built-in `java.net.http.HttpClient` API is used to make HTTP requests.

---

# Requirements

Before running the project, install:

* Java 21 or later
* Maven

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

# Installation

Clone the repository:

```bash
git clone <repository-url>
```

Move into the project directory:

```bash
cd header-scanner
```

Compile the project:

```bash
mvn clean compile
```

Run the tests:

```bash
mvn clean test
```

---

# Running the Application

The application can be launched using Maven's Exec plugin.

For a single URL:

```powershell
mvn exec:java '-Dexec.mainClass=com.ngqabutho.headerscanner.Main' '-Dexec.args=https://example.com'
```

Replace the URL with the target website you want to scan.

---

# Running Multiple Scans

Multiple URLs can be supplied as command-line arguments.

For example:

```powershell
mvn exec:java '-Dexec.mainClass=com.ngqabutho.headerscanner.Main' '-Dexec.args=https://example.com https://example.org'
```

Each URL is submitted as an independent scanning task.

---

# Example Output

A successful scan produces output similar to:

```text
==============================
HTTP Header Scanner
==============================
URL :   https://example.com
Final_URL :     https://example.com
Status_code :   200

Strict-Transport-Security
Status :        MISSING
Value :         null

Content-Security-Policy
Status :        MISSING
Value :         null

X-Content-Type-Options
Status :        MISSING
Value :         null

X-Frame-Options
Status :        MISSING
Value :         null

Referrer-Policy
Status :        MISSING
Value :         null

Permissions-Policy
Status :        MISSING
Value :         null

Score :  0
Grade :  F
```

The exact output depends on the security headers returned by the target website.

---

# Running Tests

Run the complete test suite with:

```bash
mvn clean test
```

The project uses automated tests to verify individual components as well as interactions between components.

Tests cover:

* `ScanReport`
* `Evaluator`
* `Scanner`
* `BatchRunner`
* `ReportRenderer`
* `SqliteScanHistoryRepository`

---

# Testing Strategy

The project uses several different testing approaches.

## Unit Testing

Classes containing logic such as scoring and header evaluation are tested independently.

For example, the scoring tests verify all combinations of:

```text
HIGH
MEDIUM
LOW
```

against:

```text
OK
WEAK
MISSING
```

---

## Grade Testing

The grading system is tested at the important boundaries:

```text
100 → A
80  → B
70  → C
60  → D
0   → F
```

This ensures the grade thresholds behave as intended.

---

## HTTP Testing

The scanner is tested using a local HTTP server rather than depending on an external website.

This makes the tests more predictable and avoids relying on external network availability.

---

## Batch Testing

The batch runner is tested with:

* Multiple valid URLs
* A mixture of valid and invalid URLs

The tests also verify that successful scans are persisted while failed scans are not.

---

## Persistence Testing

The SQLite repository is tested by saving records and retrieving them again.

This verifies the basic persistence round trip:

```text
Java object
    ↓
SQLite
    ↓
Java object
```

---

# Error Handling

The application distinguishes between successful scans and failed scans.

A failed URL is represented by `ScanFailure`.

Expected failures such as:

* Invalid URLs
* Network errors
* Interrupted scans

are handled without unnecessarily stopping the entire batch.

For example:

```text
URL 1 → ScanSuccess
URL 2 → ScanFailure
URL 3 → ScanSuccess
```

This allows the application to continue processing other URLs.

Thread interruption is handled by restoring the current thread's interrupt status.

---

# Design Decisions

## Java Records

Records are used for data that should primarily represent immutable values.

Examples include:

```text
HeaderRule
HeaderFinding
ScanReport
ScanRecord
ScanOptions
```

This reduces boilerplate while making the intended data-oriented nature of these classes clear.

---

## Enums

Enums are used where the application has a fixed set of valid values.

For example:

```text
Severity:
HIGH
MEDIUM
LOW
```

and:

```text
Status:
OK
WEAK
MISSING
```

Using enums prevents arbitrary strings from being used throughout the application.

---

## Repository Pattern

The `ScanHistoryRepository` interface separates persistence operations from the rest of the application.

The current implementation is SQLite-based, but the rest of the application does not need to know the database implementation details.

---

## Mapper

`ScanRecordMapper` is responsible for converting a `ScanReport` into a `ScanRecord`.

This prevents persistence-specific representation from being mixed into the scanning model.

---

## Virtual Threads

Virtual threads were selected because the scanner is primarily performing network I/O.

They allow many independent scan operations to be handled concurrently while keeping the task-based programming model straightforward.

---

# Learning Outcomes

This project provided practical experience with several Java concepts.

### Java Fundamentals

* Classes
* Objects
* Constructors
* Methods
* Access modifiers
* Packages
* Interfaces
* Enums

### Modern Java

* Records
* Pattern matching with `instanceof`
* `Optional`
* `List.of()`
* `HttpClient`
* Virtual threads

### Object-Oriented Design

The project separates responsibilities between:

* Models
* Rules
* Scanning
* Evaluation
* Batch processing
* Persistence
* Rendering
* CLI handling

### Networking

The project uses Java's HTTP Client to:

* Build HTTP requests
* Send requests
* Handle responses
* Read response headers
* Handle redirects
* Handle network-related exceptions

### Regular Expressions

Regular expressions are used where a header must contain a particular configuration.

For example, HSTS validation checks for a positive `max-age` value.

### Database Development

The project introduced:

* JDBC
* SQLite
* SQL tables
* Prepared statements
* Repository abstractions
* Mapping between Java objects and database records

### Testing

JUnit testing was used throughout development to catch regressions while changing the implementation.

### Concurrency

The project introduced practical experience with:

* `ExecutorService`
* `Future`
* Virtual threads
* Concurrent task submission
* Thread interruption
* Exception handling in asynchronous tasks

---

# Future Improvements

Possible future improvements include:

* Percentage-based scoring refinements
* More comprehensive security-header rules
* JSON output
* Exporting scan reports
* Command-line options for timeout configuration
* Scan-history querying from the CLI
* More detailed recommendations
* Improved terminal formatting
* Additional HTTP methods
* More extensive integration testing
* Configurable rule sets

These are intentionally outside the current implementation so that the core scanner remains focused and understandable.

---

# Author

**Ngqabutho Sithole**

This project was developed as a solo **WeThinkCode** elective project while studying Java and exploring cybersecurity-focused software development.

The project combines Java development with practical cybersecurity concepts, HTTP networking, automated testing, concurrency, and database persistence.
