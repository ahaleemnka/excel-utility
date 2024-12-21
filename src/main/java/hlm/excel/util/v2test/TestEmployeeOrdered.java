package hlm.excel.util.v2test;

import hlm.excel.util.v2.ExcelMapper;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class TestEmployeeOrdered {
    public static void main(String[] args) throws Exception {
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

        ExcelMapper excelMapper = new ExcelMapper();
        try (Workbook workbook = excelMapper.mapToExcel(employeeOrderedList); FileOutputStream fileOut = new FileOutputStream(EmployeeOrdered.class.getSimpleName() + ".xlsx")) {
            workbook.write(fileOut);
        }

    }

}
