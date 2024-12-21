package hlm.excel.util.v2test;

import hlm.excel.util.v2.annotation.ExcelColumn;
import hlm.excel.util.v2.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

@ExcelSheet(sheetName = "Employee New") // Sheet name for the Excel
public class EmployeeDisorder {


    // Primitive and Wrapper types
    @ExcelColumn(columnOrder = 1, header = "Employee ID")
    private int employeeId; // Primitive type

    @ExcelColumn(columnOrder = 2, header = "Employee Name")
    private String employeeName; // String

    @ExcelColumn(columnOrder = 3, header = "Is Active")
    private boolean isActive; // Primitive boolean

    @ExcelColumn
    private double salary; // Primitive type

    @ExcelColumn(header = "Age")
    private Integer age; // Wrapper class for int

    @ExcelColumn(columnOrder = 4, header = "Joining Date")
    private Date joiningDate; // Custom type Date

    @ExcelColumn(columnOrder = 5)
    private Double performanceRating; // Wrapper class for double

    @ExcelColumn(header = "Is Verified")
    private Boolean isVerified; // Wrapper class for boolean

    @ExcelColumn(columnOrder = 7, header = "Email")
    private String email; // String

    @ExcelColumn(columnOrder = 8, header = "Account Balance")
    private Float accountBalance; // Wrapper class for float

    @ExcelColumn(columnOrder = 11, header = "Department")
    private String department; // String

    @ExcelColumn(columnOrder = 9)
    private Character gender; // Wrapper class for char

    @ExcelColumn(columnOrder = 10, header = "Yearly Bonus")
    private Long yearlyBonus; // Wrapper class for long

    @ExcelColumn(columnOrder = 12)
    private String salaryInWords; // String for complex data or formatted output

    // New field types: LocalDateTime, Currency, BigDecimal

    private LocalDateTime lastLogin; // LocalDateTime (used for storing both date and time)

    @ExcelColumn(columnOrder = 14, header = "Currency")
    private Currency currency; // Currency type (e.g., USD, EUR)

    @ExcelColumn(columnOrder = 13, header = "Bonus Amount")
    private BigDecimal bonusAmount; // BigDecimal (used for precise calculations)


    // Constructor for initializing fields
    public EmployeeDisorder(int employeeId, String employeeName, boolean isActive, double salary,
                            Integer age, Date joiningDate, Double performanceRating, Boolean isVerified,
                            String email, Float accountBalance, String department, Character gender,
                            Long yearlyBonus, String salaryInWords, LocalDateTime lastLogin,
                            Currency currency, BigDecimal bonusAmount) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.isActive = isActive;
        this.salary = salary;
        this.age = age;
        this.joiningDate = joiningDate;
        this.performanceRating = performanceRating;
        this.isVerified = isVerified;
        this.email = email;
        this.accountBalance = accountBalance;
        this.department = department;
        this.gender = gender;
        this.yearlyBonus = yearlyBonus;
        this.salaryInWords = salaryInWords;
        this.lastLogin = lastLogin;
        this.currency = currency;
        this.bonusAmount = bonusAmount;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getSalary() {
        return salary;
    }

    public Integer getAge() {
        return age;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public Double getPerformanceRating() {
        return performanceRating;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public String getEmail() {
        return email;
    }

    public Float getAccountBalance() {
        return accountBalance;
    }

    public String getDepartment() {
        return department;
    }

    public Character getGender() {
        return gender;
    }

    public Long getYearlyBonus() {
        return yearlyBonus;
    }

    public String getSalaryInWords() {
        return salaryInWords;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }
}