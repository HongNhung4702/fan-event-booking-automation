package com.hongnhung.faneventbooking.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CsvDataReader {

    private static final Map<String, Map<String, Map<String, String>>> CACHE =
            new ConcurrentHashMap<>();

    private CsvDataReader() {
        // Utility class - không cho phép khởi tạo object
    }

    /**
     * Lấy toàn bộ dữ liệu của một test case từ file CSV.
     *
     * Ví dụ:
     * CsvDataReader.getRow(
     *      "testdata/login-data.csv",
     *      "SD_LOGIN_001"
     * );
     */
    public static Map<String, String> getRow(
            String resourcePath,
            String testCaseId
    ) {

        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException(
                    "resourcePath không được null hoặc rỗng."
            );
        }

        if (testCaseId == null || testCaseId.isBlank()) {
            throw new IllegalArgumentException(
                    "testCaseId không được null hoặc rỗng."
            );
        }

        String normalizedPath = normalizePath(resourcePath);

        Map<String, Map<String, String>> csvData =
                CACHE.computeIfAbsent(
                        normalizedPath,
                        CsvDataReader::loadCsv
                );

        Map<String, String> row = csvData.get(testCaseId);

        if (row == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy testCaseId '"
                            + testCaseId
                            + "' trong file '"
                            + normalizedPath
                            + "'."
            );
        }

        return row;
    }

    /**
     * Lấy một giá trị cụ thể theo testCaseId và tên cột.
     *
     * Ví dụ:
     * CsvDataReader.getValue(
     *      "testdata/login-data.csv",
     *      "SD_LOGIN_001",
     *      "username"
     * );
     */
    public static String getValue(
            String resourcePath,
            String testCaseId,
            String columnName
    ) {

        if (columnName == null || columnName.isBlank()) {
            throw new IllegalArgumentException(
                    "columnName không được null hoặc rỗng."
            );
        }

        Map<String, String> row =
                getRow(resourcePath, testCaseId);

        if (!row.containsKey(columnName)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy cột '"
                            + columnName
                            + "' trong test case '"
                            + testCaseId
                            + "'."
            );
        }

        return row.get(columnName);
    }

    /**
     * Đọc CSV từ src/test/resources.
     */
    private static Map<String, Map<String, String>> loadCsv(
            String resourcePath
    ) {

        InputStream inputStream =
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy file CSV trong resources: "
                            + resourcePath
            );
        }

        Map<String, Map<String, String>> data =
                new LinkedHashMap<>();

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        inputStream,
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.isBlank()) {
                throw new IllegalArgumentException(
                        "File CSV không có header: "
                                + resourcePath
                );
            }

            List<String> headers = parseCsvLine(headerLine);

            for (int i = 0; i < headers.size(); i++) {
                headers.set(i, headers.get(i).trim());
            }

            if (!headers.contains("testCaseId")) {
                throw new IllegalArgumentException(
                        "File CSV bắt buộc phải có cột testCaseId: "
                                + resourcePath
                );
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                List<String> values = parseCsvLine(line);

                /*
                 * Nếu dòng cuối thiếu các cột rỗng,
                 * tự bổ sung "" cho đủ số lượng header.
                 */
                while (values.size() < headers.size()) {
                    values.add("");
                }

                if (values.size() > headers.size()) {
                    throw new IllegalArgumentException(
                            "Dòng "
                                    + lineNumber
                                    + " trong file "
                                    + resourcePath
                                    + " có nhiều cột hơn header."
                    );
                }

                Map<String, String> row =
                        new LinkedHashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    row.put(
                            headers.get(i),
                            values.get(i)
                    );
                }

                String testCaseId = row.get("testCaseId");

                if (testCaseId == null
                        || testCaseId.isBlank()) {

                    throw new IllegalArgumentException(
                            "Dòng "
                                    + lineNumber
                                    + " trong file "
                                    + resourcePath
                                    + " không có testCaseId."
                    );
                }

                if (data.containsKey(testCaseId)) {
                    throw new IllegalArgumentException(
                            "testCaseId bị trùng: "
                                    + testCaseId
                                    + " trong file "
                                    + resourcePath
                    );
                }

                data.put(
                        testCaseId,
                        Collections.unmodifiableMap(row)
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Không thể đọc file CSV: "
                            + resourcePath,
                    e
            );
        }

        return Collections.unmodifiableMap(data);
    }

    /**
     * Parser CSV đơn giản nhưng hỗ trợ:
     *
     * abc,def
     * "abc,def",xyz
     * " standard_user "
     * ""
     * "Text ""quoted"""
     */
    private static List<String> parseCsvLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char currentChar = line.charAt(i);

            if (currentChar == '"') {

                /*
                 * Hai dấu "" bên trong quoted field
                 * được hiểu là một dấu ".
                 */
                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    currentValue.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (
                    currentChar == ','
                            && !insideQuotes
            ) {

                values.add(currentValue.toString());
                currentValue.setLength(0);

            } else {
                currentValue.append(currentChar);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                    "Dòng CSV có dấu ngoặc kép không hợp lệ: "
                            + line
            );
        }

        values.add(currentValue.toString());

        return values;
    }

    private static String normalizePath(String resourcePath) {

        String normalized = resourcePath.trim();

        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }

        return normalized;
    }

    /**
     * Dùng khi cần xóa cache trong quá trình debug/test.
     */
    public static void clearCache() {
        CACHE.clear();
    }
}