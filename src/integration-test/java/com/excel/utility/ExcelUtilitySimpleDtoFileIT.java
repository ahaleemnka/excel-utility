package com.excel.utility;

import com.excel.utility.dto.EmployeeOrdered;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelUtilitySimpleDtoFileIT {
    final LocalDateTime localDateTime = LocalDateTime.of(2025, Month.JANUARY, 1, 9, 0);
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    Date date;

    {
        try {
            date = sdf.parse("01-01-2020 10:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    List<String> headers = List.of("Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

    private List<EmployeeOrdered> createTestData() {
        List<EmployeeOrdered> employeeOrderedList = new ArrayList<>();

        employeeOrderedList.add(new EmployeeOrdered(1001, "John Doe", true, 60000.00, 30, date, 4.5, true,
                "john.doe@example.com", 2000.50f, "Engineering", 'M', 5000L,
                "Sixty thousand", localDateTime, Currency.getInstance("USD"),
                new BigDecimal("5000.75")));

        employeeOrderedList.add(new EmployeeOrdered(1002, "Jane Smith", false, 75000.00, 28, date, null, null,
                "jane.smith@example.com", null, "Marketing", 'F', null,
                "Seventy-five thousand", localDateTime.minusDays(1),
                Currency.getInstance("EUR"), new BigDecimal("10000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1003, "Alice Brown", true, 85000.00, 35, date, 5.0, true,
                "alice.brown@example.com", 1500.00f, "HR", 'F', 12000L,
                "Eighty-five thousand", localDateTime.minusHours(3),
                Currency.getInstance("GBP"), new BigDecimal("20000.50")));

        employeeOrderedList.add(new EmployeeOrdered(1004, "Bob White", true, 95000.00, 40, date, 4.0, false,
                "bob.white@example.com", 3000.50f, "Finance", 'M', 8000L,
                "Ninety thousand", localDateTime.minusDays(2),
                Currency.getInstance("USD"), new BigDecimal("15000.25")));

        employeeOrderedList.add(new EmployeeOrdered(1005, "Charlie Black", true, 105000.00, null, date, 3.5, true,
                "charlie.black@example.com", null, "Engineering", 'M', null,
                "One hundred thousand", localDateTime.minusDays(5),
                Currency.getInstance("AUD"), new BigDecimal("25000.75")));

        employeeOrderedList.add(new EmployeeOrdered(1006, "David Green", false, 55000.00, 45, null, 4.0, true,
                "david.green@example.com", 2500.00f, "Sales", 'M', 6000L,
                "Fifty-five thousand", localDateTime.minusMonths(1),
                Currency.getInstance("USD"), new BigDecimal("8000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1007, "Eve Blue", true, 78000.00, 33, date, 4.3, null,
                "eve.blue@example.com", 1800.00f, "IT", 'F', 10000L,
                "Seventy-eight thousand", localDateTime.minusWeeks(2),
                Currency.getInstance("GBP"), new BigDecimal("12000.50")));

        employeeOrderedList.add(new EmployeeOrdered(1008, "Frank Gray", true, 63000.00, 29, date, 4.7, true,
                "frank.gray@example.com", 2200.00f, "Operations", 'M', null,
                "Sixty-three thousand", localDateTime.minusMinutes(30),
                Currency.getInstance("EUR"), new BigDecimal("11000.25")));

        employeeOrderedList.add(new EmployeeOrdered(1009, "Grace Yellow", false, 95000.00, 38, date, 4.1, true,
                "grace.yellow@example.com", 2700.00f, "Support", 'F', 11000L,
                "Ninety-five thousand", localDateTime.minusDays(10),
                Currency.getInstance("USD"), new BigDecimal("17000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1010, "Henry Red", true, 52000.00, 50, date, 3.8, false,
                "henry.red@example.com", 2000.00f, "Logistics", 'M', 15000L,
                "Fifty-two thousand", localDateTime.minusMonths(3),
                Currency.getInstance("AUD"), new BigDecimal("13000.10")));

        return employeeOrderedList;
    }

    private void validateHeaders(Sheet sheet, String... expectedHeaders) {
        Row headerRow = sheet.getRow(0);
        for (int i = 0; i < expectedHeaders.length; i++) {
            assertEquals(expectedHeaders[i], headerRow.getCell(i).getStringCellValue(), "Header validation failed for : " + expectedHeaders[i]);
        }
    }

    private void validateRow(Row row, String... expectedValues) {
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], row.getCell(i).getStringCellValue(), headers.get(i) + " is not correct at Row : " + row.getRowNum());
        }
    }

    @Test
    void testExcelMappingAndValidationForHSSFWorkbook() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToHSSFWorkbook(employeeOrderedList);

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xls");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new HSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }

    @Test
    void testExcelMappingAndValidationForXSSFWorkbook() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToXSSFWorkbook(employeeOrderedList);

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new XSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }

    @Test
    void testExcelMappingAndValidationForSXSSFWorkbook() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToSXSSFWorkbook(employeeOrderedList);

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new XSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }

    @Test
    void testExcelMappingAndValidationForHSSFWorkbookStream() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToHSSFWorkbook(employeeOrderedList.stream());

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xls");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new HSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }

    @Test
    void testExcelMappingAndValidationForXSSFWorkbookStream() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToXSSFWorkbook(employeeOrderedList.stream());

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new XSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }

    @Test
    void testExcelMappingAndValidationForSXSSFWorkbookStream() throws Exception {
        // Create test data
        List<EmployeeOrdered> employeeOrderedList = createTestData();

        // Generate Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToSXSSFWorkbook(employeeOrderedList.stream());

        // Save the workbook to the file system
        File file = new File("target/EmployeeOrderedTest.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }

        // Reload the workbook from the file
        try (FileInputStream fileIn = new FileInputStream(file)) {
            Workbook loadedWorkbook = new XSSFWorkbook(fileIn);
            Sheet sheet = loadedWorkbook.getSheetAt(0);

            // Validate the headers
            validateHeaders(sheet, "Employee ID", "Employee Name", "Is Active", "Salary", "Age", "Joining Date", "Performance Rating", "Eligible", "Email", "Account Balance", "Department", "Gender", "Yearly Bonus", "Salary in Words", "Last Updated", "Currency", "Bonus Amount");

            // Validate the contents of the Excel file for each row
            validateRow(sheet.getRow(1), "1001", "John Doe", "true", "60000.0", "30", date.toString(), "4.5", "true", "john.doe@example.com", "2000.5", "Engineering", "M", "5000", "Sixty thousand", localDateTime.toString(), "USD", "5000.75");
            validateRow(sheet.getRow(2), "1002", "Jane Smith", "false", "75000.0", "28", date.toString(), "", "", "jane.smith@example.com", "", "Marketing", "F", "", "Seventy-five thousand", localDateTime.minusDays(1).toString(), "EUR", "10000.00");
            validateRow(sheet.getRow(3), "1003", "Alice Brown", "true", "85000.0", "35", date.toString(), "5.0", "true", "alice.brown@example.com", "1500.0", "HR", "F", "12000", "Eighty-five thousand", localDateTime.minusHours(3).toString(), "GBP", "20000.50");
            validateRow(sheet.getRow(4), "1004", "Bob White", "true", "95000.0", "40", date.toString(), "4.0", "false", "bob.white@example.com", "3000.5", "Finance", "M", "8000", "Ninety thousand", localDateTime.minusDays(2).toString(), "USD", "15000.25");
            validateRow(sheet.getRow(5), "1005", "Charlie Black", "true", "105000.0", "", date.toString(), "3.5", "true", "charlie.black@example.com", "", "Engineering", "M", "", "One hundred thousand", localDateTime.minusDays(5).toString(), "AUD", "25000.75");
            validateRow(sheet.getRow(6), "1006", "David Green", "false", "55000.0", "45", "", "4.0", "true", "david.green@example.com", "2500.0", "Sales", "M", "6000", "Fifty-five thousand", localDateTime.minusMonths(1).toString(), "USD", "8000.00");
            validateRow(sheet.getRow(7), "1007", "Eve Blue", "true", "78000.0", "33", date.toString(), "4.3", "", "eve.blue@example.com", "1800.0", "IT", "F", "10000", "Seventy-eight thousand", localDateTime.minusWeeks(2).toString(), "GBP", "12000.50");
            validateRow(sheet.getRow(8), "1008", "Frank Gray", "true", "63000.0", "29", date.toString(), "4.7", "true", "frank.gray@example.com", "2200.0", "Operations", "M", "", "Sixty-three thousand", localDateTime.minusMinutes(30).toString(), "EUR", "11000.25");
            validateRow(sheet.getRow(9), "1009", "Grace Yellow", "false", "95000.0", "38", date.toString(), "4.1", "true", "grace.yellow@example.com", "2700.0", "Support", "F", "11000", "Ninety-five thousand", localDateTime.minusDays(10).toString(), "USD", "17000.00");
            validateRow(sheet.getRow(10), "1010", "Henry Red", "true", "52000.0", "50", date.toString(), "3.8", "false", "henry.red@example.com", "2000.0", "Logistics", "M", "15000", "Fifty-two thousand", localDateTime.minusMonths(3).toString(), "AUD", "13000.10");
        }

        // Cleanup the test file
        file.delete();
    }
}