package hlm.excel.util.v2test;

import hlm.excel.util.v2.ExcelMapper;
import hlm.excel.util.v2test.complex.EmployeeComplex;
import hlm.excel.util.v2test.complex.Project;
import hlm.excel.util.v2test.complex.Role;
import hlm.excel.util.v2test.complex.SalaryDetails;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

public class TestEmployeeComplex {

    public static void main(String[] args) {
        List<EmployeeComplex> employeeComplexList = new ArrayList<>();

        employeeComplexList.add(new EmployeeComplex(
                1001,
                "John Doe",
                "Engineering",
                new Role("Developer", "Senior"),
                Arrays.asList("Java", "Spring", "SQL"),
                Map.of("Email", "john.doe@example.com", "Phone", "123-456-7890"),
                Arrays.asList(new Project("Project A", 12), new Project("Project B", 8)),
                new SalaryDetails(new BigDecimal("60000.00"), new BigDecimal("5000.00"), new BigDecimal("200.00"))
        ));

        employeeComplexList.add(new EmployeeComplex(
                1002,
                "Jane Smith",
                "Marketing",
                new Role("Analyst", "Junior"),
                Arrays.asList("SEO", "Content Marketing"),
                Map.of("Email", "jane.smith@example.com"),
                Collections.singletonList(new Project("Campaign X", 6)),
                new SalaryDetails(new BigDecimal("75000.00"), new BigDecimal("7000.00"), new BigDecimal("300.00"))
        ));

        employeeComplexList.add(new EmployeeComplex(
                1003,
                "Alice Brown",
                "HR",
                new Role("HR Manager", "Mid-level"),
                Arrays.asList("Recruitment", "Policy Design"),
                Map.of("Email", "alice.brown@example.com", "Phone", "789-123-4567"),
                Arrays.asList(new Project("Hiring Drive", 4), new Project("Employee Handbook", 2)),
                new SalaryDetails(new BigDecimal("85000.00"), new BigDecimal("12000.00"), new BigDecimal("500.00"))
        ));

        employeeComplexList.add(new EmployeeComplex(
                1004,
                "Bob White",
                "Finance",
                null,
                Arrays.asList("Excel", "Accounting", "Auditing"),
                Map.of("Email", "bob.white@example.com"),
                Collections.emptyList(),
                new SalaryDetails(new BigDecimal("95000.00"), new BigDecimal("10000.00"), new BigDecimal("600.00"))
        ));

        employeeComplexList.add(new EmployeeComplex(
                1005,
                "Charlie Black",
                "Engineering",
                new Role("Lead Engineer", "Senior"),
                Arrays.asList("Python", "Django"),
                Map.of("Email", "charlie.black@example.com", "LinkedIn", "linkedin.com/charlie-black"),
                Arrays.asList(new Project("AI System", 24)),
                new SalaryDetails(new BigDecimal("105000.00"), new BigDecimal("15000.00"), new BigDecimal("1000.00"))
        ));

        // Convert the list to Excel using ExcelMapper
        ExcelMapper excelMapper = new ExcelMapper();
        try (Workbook workbook = excelMapper.mapToExcel(employeeComplexList);
             FileOutputStream fileOut = new FileOutputStream(EmployeeComplex.class.getSimpleName() + ".xlsx")) {
            workbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
