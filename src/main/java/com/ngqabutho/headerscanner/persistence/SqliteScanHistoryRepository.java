package com.ngqabutho.headerscanner.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteScanHistoryRepository implements ScanHistoryRepository {

    private static final String DB_URL = "jdbc:sqlite:data/scans.db";

    public SqliteScanHistoryRepository() {
        try{
            createTable();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection connect() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }

    private void createTable() throws SQLException {
        try(Connection connection = connect(); Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("""
                     CREATE TABLE IF NOT EXISTS scans (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            url TEXT NOT NULL,
                            final_url TEXT NOT NULL,
                            status_code INTEGER NOT NULL,
                            score INTEGER NOT NULL,
                            grade TEXT NOT NULL
                        )
                        """
            );
        }
    }

    @Override
    public void save(ScanRecord record) {
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement("""
                     INSERT INTO scans (url, final_url, status_code, score, grade) VALUES(?,?,?,?,?)
                     """
             )) {

            stmt.setString(1, record.url());
            stmt.setString(2, record.finalUrl());
            stmt.setInt(3, record.statusCode());
            stmt.setInt(4, record.score());
            stmt.setString(5, String.valueOf(record.grade()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ScanRecord> findAll() {
        try (Connection connection = connect();
        PreparedStatement pstmt = connection.prepareStatement("""
                SELECT id, url, final_url, status_code, score, grade FROM scans
                """);
        ResultSet rs = pstmt.executeQuery()
        ) {
            List<ScanRecord> records = new ArrayList<>();

            while (rs.next()){
                long id = rs.getLong("id");
                String url = rs.getString("url");
                String finalUrl = rs.getString("final_url");
                int statusCode = rs.getInt("status_code");
                int score = rs.getInt("score");
                String gradeString = rs.getString("grade");
                char grade = gradeString.charAt(0);

                records.add(new ScanRecord(id, url, finalUrl, statusCode, score,grade));
            }

            return records;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
