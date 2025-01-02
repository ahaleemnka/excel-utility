package com.excel.utility.util;

import com.excel.utility.dto.ColumnMetadata;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class CellUtils {

    public static CellStyle styleForHeader(Sheet sheet) {
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    public static void autoSize(Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        columnMetadataList.forEach(metadata -> sheet.autoSizeColumn(metadata.getColumnOrder()));
    }
}
