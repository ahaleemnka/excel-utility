package com.excel.utility.v2test.complex;

import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.annotation.ExcelColumn;

import java.math.BigDecimal;

@ExcelMapper
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
