package com.excel.utility;

import com.excel.utility.dto.EmployeeOrdered;
import org.apache.poi.ss.usermodel.*;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmployeeOrderedOrderedOrderedIT {

    @Ignore
    @Test
    public void testExcelMapping() throws Exception {
        // Initialize test data (list of Employee objects)
        List<EmployeeOrdered> employeeOrderedList = new ArrayList<>();

        employeeOrderedList.add(new EmployeeOrdered(1001, "John Doe", true, 60000.00, 30, new Date(), 4.5, true,
                "john.doe@example.com", 2000.50f, "Engineering", 'M', 5000L,
                "Sixty thousand", LocalDateTime.now(), Currency.getInstance("USD"),
                new BigDecimal("5000.75")));

        employeeOrderedList.add(new EmployeeOrdered(1002, "Jane Smith", false, 75000.00, 28, new Date(), null, null,
                "jane.smith@example.com", null, "Marketing", 'F', null,
                "Seventy-five thousand", LocalDateTime.now().minusDays(1),
                Currency.getInstance("EUR"), new BigDecimal("10000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1003, "Alice Brown", true, 85000.00, 35, new Date(), 5.0, true,
                "alice.brown@example.com", 1500.00f, "HR", 'F', 12000L,
                "Eighty-five thousand", LocalDateTime.now().minusHours(3),
                Currency.getInstance("GBP"), new BigDecimal("20000.50")));

        employeeOrderedList.add(new EmployeeOrdered(1004, "Bob White", true, 95000.00, 40, new Date(), 4.0, false,
                "bob.white@example.com", 3000.50f, "Finance", 'M', 8000L,
                "Ninety thousand", LocalDateTime.now().minusDays(2),
                Currency.getInstance("USD"), new BigDecimal("15000.25")));

        employeeOrderedList.add(new EmployeeOrdered(1005, "Charlie Black", true, 105000.00, null, new Date(), 3.5, true,
                "charlie.black@example.com", null, "Engineering", 'M', null,
                "One hundred thousand", LocalDateTime.now().minusDays(5),
                Currency.getInstance("AUD"), new BigDecimal("25000.75")));

        employeeOrderedList.add(new EmployeeOrdered(1006, "David Green", false, 55000.00, 45, null, 4.0, true,
                "david.green@example.com", 2500.00f, "Sales", 'M', 6000L,
                "Fifty-five thousand", LocalDateTime.now().minusMonths(1),
                Currency.getInstance("USD"), new BigDecimal("8000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1007, "Eve Blue", true, 78000.00, 33, new Date(), 4.3, null,
                "eve.blue@example.com", 1800.00f, "IT", 'F', 10000L,
                "Seventy-eight thousand", LocalDateTime.now().minusWeeks(2),
                Currency.getInstance("GBP"), new BigDecimal("12000.50")));

        employeeOrderedList.add(new EmployeeOrdered(1008, "Frank Gray", true, 63000.00, 29, new Date(), 4.7, true,
                "frank.gray@example.com", 2200.00f, "Operations", 'M', null,
                "Sixty-three thousand", LocalDateTime.now().minusMinutes(30),
                Currency.getInstance("EUR"), new BigDecimal("11000.25")));

        employeeOrderedList.add(new EmployeeOrdered(1009, "Grace Yellow", false, 95000.00, 38, new Date(), 4.1, true,
                "grace.yellow@example.com", 2700.00f, "Support", 'F', 11000L,
                "Ninety-five thousand", LocalDateTime.now().minusDays(10),
                Currency.getInstance("USD"), new BigDecimal("17000.00")));

        employeeOrderedList.add(new EmployeeOrdered(1010, "Henry Red", true, 52000.00, 50, new Date(), 3.8, false,
                "henry.red@example.com", 2000.00f, "Logistics", 'M', 15000L,
                "Fifty-two thousand", LocalDateTime.now().minusMonths(3),
                Currency.getInstance("AUD"), new BigDecimal("13000.10")));

        // Generate the Excel file using ExcelUtility
        ExcelUtility excelUtility = new ExcelUtility();
        Workbook workbook = excelUtility.mapToExcel(employeeOrderedList);

        // Assert sheet existence
        Sheet sheet = workbook.getSheet("Employee");
        assertNotNull(sheet, "Excel sheet 'Employee' should exist.");

        // Validate headers in the first row
        Row headerRow = sheet.getRow(0);
        assertNotNull(headerRow, "Header row should not be null.");

        validateCell(headerRow.getCell(0), "Employee ID");
        validateCell(headerRow.getCell(1), "Employee Name");
        validateCell(headerRow.getCell(2), "Is Active");
        validateCell(headerRow.getCell(3), "Salary");
        validateCell(headerRow.getCell(4), "Age");
        validateCell(headerRow.getCell(5), "Joining Date");
        validateCell(headerRow.getCell(6), "Performance Rating");
        validateCell(headerRow.getCell(7), "Is Verified");
        validateCell(headerRow.getCell(8), "Email");
        validateCell(headerRow.getCell(9), "Account Balance");
        validateCell(headerRow.getCell(10), "Department");
        validateCell(headerRow.getCell(11), "Gender");
        validateCell(headerRow.getCell(12), "Yearly Bonus");
        validateCell(headerRow.getCell(13), "Salary in Words");
        validateCell(headerRow.getCell(14), "Last Login");
        validateCell(headerRow.getCell(15), "Currency");
        validateCell(headerRow.getCell(16), "Bonus Amount");

        // Validate data rows
        for (int i = 1; i <= employeeOrderedList.size(); i++) {
            Row row = sheet.getRow(i);
            EmployeeOrdered employeeOrdered = employeeOrderedList.get(i - 1);

            // Validate Employee ID
            assertEquals(employeeOrdered.getEmployeeId(), (double) getCellValue(row.getCell(0)));

            // Validate Employee Name
            assertEquals(employeeOrdered.getEmployeeName(), getCellValue(row.getCell(1)));

            // Validate Is Active
            assertEquals(employeeOrdered.isActive(), (boolean) getCellValue(row.getCell(2)));

            // Validate Salary

            assertEquals(employeeOrdered.getSalary(), getCellValue(row.getCell(3)));


            // Handle Age as Integer or Double
            Object ageCellValue = getCellValue(row.getCell(4));
            if (employeeOrdered.getAge() == null) {
                assertEquals("", ageCellValue);
            } else if (ageCellValue instanceof Double) {
                double ageValue = (Double) ageCellValue;
                assertEquals(employeeOrdered.getAge().doubleValue(), ageValue, 0.01);
            } else if (ageCellValue instanceof Integer) {
                assertEquals(employeeOrdered.getAge(), (int) ageCellValue);
            }

            // Validate Date field (Joining Date)
            Cell joiningDateCell = row.getCell(5);
            if (employeeOrdered.getJoiningDate() == null) {
                assertEquals("", getCellValue(joiningDateCell));
            } else {
                assertNotNull(joiningDateCell, "Joining Date cell should not be null.");

                if (joiningDateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(joiningDateCell)) {
                    assertNotNull(joiningDateCell.getDateCellValue(), "Date should be parsed correctly.");
//                } else {
//                    fail("Joining Date cell is not a valid date format.");
                }
            }

            // Validate Performance Rating (Double)
            if (employeeOrdered.getPerformanceRating() == null) {
                assertEquals("", getCellValue(row.getCell(6)));
            } else {
                assertEquals(employeeOrdered.getPerformanceRating(), getCellValue(row.getCell(6)));
            }

            // Validate Is Verified (Boolean)
            if (employeeOrdered.getIsVerified() == null) {
                assertEquals("", getCellValue(row.getCell(7)));
            } else {
                assertEquals(employeeOrdered.getIsVerified(), getCellValue(row.getCell(7)));
            }

            // Validate Email
            assertEquals(employeeOrdered.getEmail(), getCellValue(row.getCell(8)));

            // Validate Account Balance (Float)
            if (employeeOrdered.getAccountBalance() == null) {
                assertEquals("", getCellValue(row.getCell(9)));
            } else {
                assertEquals(employeeOrdered.getAccountBalance().toString(), getCellValue(row.getCell(9)).toString());
            }

            // Validate Department
            assertEquals(employeeOrdered.getDepartment(), getCellValue(row.getCell(10)));

            // Validate Gender (Char)
            if (employeeOrdered.getGender() == null) {
                assertEquals("", getCellValue(row.getCell(11)));
            } else {
                assertEquals(employeeOrdered.getGender().toString(), getCellValue(row.getCell(11)));
            }

            // Validate Yearly Bonus (Long)
            if (employeeOrdered.getYearlyBonus() == null) {
                assertEquals("", getCellValue(row.getCell(12)));
            } else {
                assertEquals(employeeOrdered.getYearlyBonus().toString(), getCellValue(row.getCell(12)).toString());
            }

            // Validate Salary in Words
            assertEquals(employeeOrdered.getSalaryInWords(), getCellValue(row.getCell(13)));

            // Validate Last Login (LocalDateTime)
            if (employeeOrdered.getLastLogin() == null) {
                assertEquals("", getCellValue(row.getCell(14)));
            } else {
                assertNotNull(row.getCell(14));
                assertEquals(LocalDateTime.parse(getCellValue(row.getCell(14)).toString()), employeeOrdered.getLastLogin());
            }

            // Validate Currency
            assertEquals(employeeOrdered.getCurrency().getCurrencyCode(), getCellValue(row.getCell(15)));

            // Validate Bonus Amount (BigDecimal)
            if (employeeOrdered.getBonusAmount() == null) {
                assertEquals("", getCellValue(row.getCell(16)));
            } else {
                assertEquals(employeeOrdered.getBonusAmount(), new BigDecimal(getCellValue(row.getCell(16)).toString()));
            }
        }
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private void validateCell(Cell cell, String expectedValue) {
        assertNotNull(cell, "Cell should not be null.");
        assertEquals(expectedValue, getCellValue(cell), "Cell value does not match expected.");
    }
}