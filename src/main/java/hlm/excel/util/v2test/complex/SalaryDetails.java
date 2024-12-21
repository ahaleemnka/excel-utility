package hlm.excel.util.v2test.complex;

import hlm.excel.util.v2.annotation.ExcelColumn;
import hlm.excel.util.v2.annotation.ExcelSheet;

import java.math.BigDecimal;

@ExcelSheet
public class SalaryDetails {
    @ExcelColumn(columnOrder = 11, header = "Base Salary")
    private BigDecimal baseSalary;

    @ExcelColumn( header = "Bonus")
    private BigDecimal bonus;

    @ExcelColumn( header = "Deductions")
    private BigDecimal deductions;

    // Constructor
    public SalaryDetails(BigDecimal baseSalary, BigDecimal bonus, BigDecimal deductions) {
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
    }

    // Getters
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }
}
