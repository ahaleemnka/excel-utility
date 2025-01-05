//package com.excel.utility;
//
//import com.excel.utility.dto.EmployeeRandom;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class EmployeeOrderedOrderedRandomOrderIT {
//
//
//    @Ignore
//    @Test
//    void testDynamicColumnOrderingAndHeaderNameGeneration() throws Exception {
//        // Create test data with some fields deliberately out of order or missing
//        List<EmployeeRandom> employeeList = new ArrayList<>();
//        employeeList.add(new EmployeeRandom(
//                1, "John Doe", true, 50000.0, 30, new Date(), 4.5,
//                true, "john.doe@example.com", 10000.0f, "Engineering", 'M',
//                5000L, "Fifty Thousand", LocalDateTime.now(), Currency.getInstance("USD"), new BigDecimal("1000.50")
//        ));
//        employeeList.add(new EmployeeRandom(
//                2, "Jane Smith", false, 45000.0, 28, new Date(), 4.8,
//                false, "jane.smith@example.com", 12000.0f, "HR", 'F',
//                4500L, "Forty-Five Thousand", LocalDateTime.now(), Currency.getInstance("EUR"), new BigDecimal("900.75")
//        ));
//
//        // Use ExcelUtility to generate the Excel workbook
//        ExcelUtility excelUtility = new ExcelUtility();
//        Workbook workbook = excelUtility.mapToExcel(employeeList);
//
//        // Validate the workbook content
//        assertNotNull(workbook, "Workbook should not be null");
//        assertEquals(1, workbook.getNumberOfSheets(), "There should be one sheet");
//        assertEquals("Employee New", workbook.getSheetName(0), "Sheet name should match");
//
//        // Validate column headers
//        List<String> expectedHeaders = Arrays.asList(
//                "Employee ID", "Employee Name", "Is Active", "Joining Date", "Performance Rating",
//                "Salary", "Email", "Account Balance", "Gender", "Yearly Bonus", "Department",
//                "Salary in Words", "Bonus Amount", "Currency", "Age", "Is Verified", "Last Login Time"
//        );
//
//        var sheet = workbook.getSheetAt(0);
//        var headerRow = sheet.getRow(0);
//        assertNotNull(headerRow, "Header row should not be null");
//        assertEquals(expectedHeaders.size(), headerRow.getPhysicalNumberOfCells(), "Header size should match");
//
//        for (int i = 0; i < expectedHeaders.size(); i++) {
//            assertEquals(expectedHeaders.get(i), headerRow.getCell(i).getStringCellValue(),
//                    "Header name at column " + (i + 1) + " should match");
//        }
//
//        // Validate data rows
//        for (int rowIndex = 1; rowIndex <= employeeList.size(); rowIndex++) {
//            var row = sheet.getRow(rowIndex);
//            assertNotNull(row, "Row " + rowIndex + " should not be null");
//
//            EmployeeRandom employee = employeeList.get(rowIndex - 1);
//
//            assertEquals(employee.getEmployeeId(), (int) row.getCell(0).getNumericCellValue(), "Employee ID should match");
//            assertEquals(employee.getEmployeeName(), row.getCell(1).getStringCellValue(), "Employee Name should match");
//            assertEquals(employee.isActive(), row.getCell(2).getBooleanCellValue(), "Is Active should match");
//            //assertNotNull(row.getCell(3).getDateCellValue(), "Joining Date should not be null");
//            assertEquals(employee.getPerformanceRating(), row.getCell(4).getNumericCellValue(), "Performance Rating should match");
//            assertEquals(employee.getSalary(), row.getCell(5).getNumericCellValue(), "Salary should match");
//            assertEquals(employee.getEmail(), row.getCell(6).getStringCellValue(), "Email should match");
//            assertEquals(employee.getAccountBalance().toString(), row.getCell(7).getStringCellValue(), "Account Balance should match");
//            assertEquals(employee.getGender().toString(), row.getCell(8).getStringCellValue(), "Gender should match");
//            assertEquals(employee.getYearlyBonus().toString(), row.getCell(9).toString(), "Yearly Bonus should match");
//            assertEquals(employee.getDepartment(), row.getCell(10).getStringCellValue(), "Department should match");
//            assertEquals(employee.getSalaryInWords(), row.getCell(11).getStringCellValue(), "Salary in Words should match");
//            assertEquals(employee.getBonusAmount().toString(), row.getCell(12).toString(), "Bonus Amount should match");
//            assertEquals(employee.getCurrency().getCurrencyCode(), row.getCell(13).getStringCellValue(), "Currency should match");
//            assertEquals(employee.getAge(), (int) row.getCell(14).getNumericCellValue(), "Age should match");
//            assertEquals(employee.getIsVerified(), row.getCell(15).getBooleanCellValue(), "Is Verified should match");
//            assertNotNull(row.getCell(16).getStringCellValue(), "Last Login Time should not be null");
//        }
//    }
//}
