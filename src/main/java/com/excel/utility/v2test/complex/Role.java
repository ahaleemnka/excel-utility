package com.excel.utility.v2test.complex;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;

@ExcelMapper()
public class Role {
    @ExcelColumn(columnOrder = 5, header = "Role Name")
    private String roleName;

    @ExcelColumn(columnOrder = 6, header = "Experience Level")
    private String experienceLevel;

    // Constructor
    public Role(String roleName, String experienceLevel) {
        this.roleName = roleName;
        this.experienceLevel = experienceLevel;
    }

    // Getters
    public String getRoleName() {
        return roleName;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }
}