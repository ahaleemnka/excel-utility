package com.excel.utility.dto;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class ColumnMetadata {
    private final Field field;
    private final String header;
    private int columnOrder;
    private final Class<?> fieldType;

    private List<Field> parentClassFieldList;

    public ColumnMetadata(Field field, String header, int columnOrder, Class<?> fieldType, List<Field> parentClassFieldList) {
        this.field = field;
        this.header = header;
        this.columnOrder = columnOrder;
        this.fieldType = fieldType;
        this.parentClassFieldList = parentClassFieldList;
    }

    public List<Field> getParentClassFieldList() {
        return parentClassFieldList;
    }

    public void setParentClassFieldList(List<Field> parentClassFieldList) {
        this.parentClassFieldList = Collections.unmodifiableList(parentClassFieldList);
    }

    public Field getField() {
        return field;
    }

    public String getHeader() {
        return header;
    }

    public int getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(int columnOrder) {
        this.columnOrder = columnOrder;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }
}
