package com.excel.utility.v2test.complex;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;

import java.util.List;
import java.util.Map;

@ExcelMapper(sheetName = "EmployeeComplex") // Sheet name for the Excel
public class EmployeeComplex {

    // Primitive and Wrapper types
    @ExcelColumn(columnOrder = 1, header = "Employee ID")
    private int employeeId; // Primitive type

    @ExcelColumn(columnOrder = 2, header = "Employee Name")
    private String employeeName; // String

    @ExcelColumn(columnOrder = 3, header = "Department")
    private String department; // String

    @ExcelColumn(columnOrder = 4, header = "Role")
    private Role role; // Inner object

    @ExcelColumn(columnOrder = 7, header = "Skills")
    private List<String> skills; // List of strings

    @ExcelColumn(columnOrder = 8, header = "Contacts")
    private Map<String, String> contacts; // Map of contact types to values

    @ExcelColumn(columnOrder = 9, header = "Projects")
    private List<Project> projects; // List of inner objects

    @ExcelColumn(columnOrder = 10, header = "Salary Details")
    private SalaryDetails salaryDetails; // Inner object with nested data

    // Constructor for initializing fields
    public EmployeeComplex(int employeeId, String employeeName, String department, Role role,
                           List<String> skills, Map<String, String> contacts, List<Project> projects,
                           SalaryDetails salaryDetails) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.department = department;
        this.role = role;
        this.skills = skills;
        this.contacts = contacts;
        this.projects = projects;
        this.salaryDetails = salaryDetails;
    }

    // Getters for fields (only for testing dynamic access via reflection)
    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public Role getRole() {
        return role;
    }

    public List<String> getSkills() {
        return skills;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public SalaryDetails getSalaryDetails() {
        return salaryDetails;
    }
}
