package com.excel.utility.dto;

import com.excel.utility.annotation.ExcelColumn;
import com.excel.utility.annotation.ExcelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ExcelMapper// Add this annotation with the correct sheet name
public class ListAndMapTestDto {

    @ExcelColumn(columnOrder = 1)
    private int id;

    @ExcelColumn(columnOrder = 2)
    private String name;

    @ExcelColumn(columnOrder = 3)
    private List<String> roles;

    @ExcelColumn(columnOrder = 4)
    private Map<String, String> attributes;

    @ExcelColumn(columnOrder = 5)
    private LocalDateTime timestamp;

    @ExcelColumn(columnOrder = 6)
    private BigDecimal salary;

    public ListAndMapTestDto(int id, String name, List<String> roles, Map<String, String> attributes,
                   LocalDateTime timestamp, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.attributes = attributes;
        this.timestamp = timestamp;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
