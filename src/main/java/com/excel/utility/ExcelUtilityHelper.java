package com.excel.utility;

import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.extractor.ColumnMetadataExtractor;
import com.excel.utility.extractor.FieldExtractor;
import com.excel.utility.processor.ObjectValueProcessor;
import com.excel.utility.util.CellUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code ExcelUtilityHelper} class provides internal support for mapping POJOs to Excel workbooks
 * across different formats (XSSFWorkbook, SXSSFWorkbook, HSSFWorkbook).
 * <p>
 * This class extracts metadata from annotated POJO classes and uses it to generate Excel sheets
 * with headers and data rows. It ensures consistent handling of different Excel formats
 * while maintaining scalability for future enhancements.
 */
class ExcelUtilityHelper {

    private final FieldExtractor fieldExtractor;
    private final ObjectValueProcessor objectValueProcessor;

    protected ExcelUtilityHelper(FieldExtractor fieldExtractor, ObjectValueProcessor objectValueProcessor) {
        this.fieldExtractor = fieldExtractor;
        this.objectValueProcessor = objectValueProcessor;
    }

    /**
     * Maps a stream of POJOs to an Excel workbook.
     *
     * @param dataStream The stream of data objects.
     * @param sheetName  The name of the sheet to be created.
     * @param workbook   The target workbook (XSSFWorkbook, SXSSFWorkbook, or HSSFWorkbook).
     * @param <T>        Type of the workbook.
     * @return The populated workbook.
     */
    protected <T extends Workbook> T mapToWorkbook(Stream<?> dataStream, String sheetName, T workbook) {
        validateInputs(dataStream, workbook);

        List<?> dataList = dataStream.collect(Collectors.toList());
        Class<?> clazz = extractClassFromDataList(dataList);

        validatePOJO(clazz);

        Sheet sheet = workbook.createSheet(sheetName);

        // Extract column metadata and populate sheet
        ColumnMetadataExtractor columnMetadataExtractor = new ColumnMetadataExtractor();
        List<ColumnMetadata> columnMetadataList = columnMetadataExtractor.process(clazz);

        populateHeader(sheet, columnMetadataList);
        populateRows(dataList.stream(), sheet, columnMetadataList);

        return workbook;
    }

    /**
     * Validates inputs for the mapping operation.
     */
    private void validateInputs(Stream<?> dataStream, Workbook workbook) {
        if (workbook == null) {
            throw new IllegalArgumentException("The workbook cannot be null.");
        }
        if (Objects.isNull(dataStream)) {
            throw new IllegalArgumentException("The data stream cannot be null.");
        }
    }

    /**
     * Extracts the class type from the data list.
     */
    private Class<?> extractClassFromDataList(List<?> dataList) {
        return dataList.stream()
                .findFirst()
                .map(Object::getClass)
                .orElseThrow(() -> new IllegalArgumentException("The data list is empty."));
    }

    /**
     * Validates that the POJO class has the required annotation.
     */
    private void validatePOJO(Class<?> clazz) {
        if (clazz.getAnnotation(ExcelMapper.class) == null) {
            throw new IllegalArgumentException("POJO class must be annotated with @ExcelMapper.");
        }
    }

    /**
     * Populates the header row in the Excel sheet.
     */
    private void populateHeader(Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        Row headerRow = sheet.createRow(Config.ROW_VALUE_FOR_HEADER);
        CellStyle headerStyle = CellUtils.styleForHeader(sheet);

        for (ColumnMetadata columnMetadata : columnMetadataList) {
            Cell cell = headerRow.createCell(columnMetadata.getColumnOrder() - 1);
            cell.setCellValue(columnMetadata.getHeader());
            cell.setCellStyle(headerStyle);
        }

        CellUtils.autoSize(sheet, columnMetadataList);
    }

    /**
     * Populates the data rows in the Excel sheet.
     */
    private void populateRows(Stream<?> dataStream, Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        AtomicInteger rowIndex = new AtomicInteger(Config.ROW_VALUE_START_FOR_DATA);

        dataStream.forEach(data -> {
            Row row = sheet.createRow(rowIndex.getAndIncrement());

            for (ColumnMetadata columnMetadata : columnMetadataList) {
                Object value = fieldExtractor.process(columnMetadata, data);
                String cellValue = processValue(value);

                Cell cell = row.createCell(columnMetadata.getColumnOrder() - 1);
                cell.setCellValue(cellValue);
            }
        });
    }

    /**
     * Processes the field value into a string, handling null and overly long values.
     */
    private String processValue(Object value) {
        String cellValue = objectValueProcessor.process(value);
        if (cellValue != null && cellValue.length() > 32767) {
            return cellValue.substring(0, 32760); // Excel cell value limit
        }
        return cellValue;
    }
}
