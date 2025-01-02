package com.excel.utility;

import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.extractor.ColumnMetadataExtractor;
import com.excel.utility.extractor.FieldExtractor;
import com.excel.utility.util.CellUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The ExcelUtility class is responsible for converting a list of POJOs into an Excel workbook representation.
 * It maps the data fields of a POJO to a sheet in the Excel workbook, with each field being represented by a column.
 * The class uses annotations to extract metadata for the sheet and column headers, and handles the population of both the header and data rows in the Excel sheet.
 */
public class ExcelUtility {

    // Field extractor to retrieve field values based on the metadata
    private final FieldExtractor fieldExtractor;

    /**
     * Constructor that initializes the FieldExtractor.
     */
    public ExcelUtility() {
        fieldExtractor = new FieldExtractor();
    }

    /**
     * Converts a list of POJOs into an Excel workbook.
     * The list should contain objects of a class annotated with @ExcelMapper to map the sheet name and columns.
     *
     * @param dataList the list of POJOs to map to the Excel workbook.
     * @return a Workbook object containing the data.
     * @throws IllegalArgumentException if the data list is empty or the POJO class is not annotated with @ExcelMapper.
     */
    public Workbook mapToExcel(List<?> dataList) {
        // Delegate to the Stream-based implementation
        return mapToExcel(dataList.stream());
    }

    /**
     * Converts a stream of POJOs into an Excel workbook.
     * The stream should contain objects of a class annotated with @ExcelMapper to map the sheet name and columns.
     * <p>
     * This method supports streaming large datasets efficiently without requiring the entire dataset to be loaded
     * into memory at once. Each object in the stream will be processed incrementally to populate the Excel sheet.
     *
     * @param dataStream the stream of POJOs to map to the Excel workbook.
     * @return a Workbook object containing the data.
     * @throws IllegalArgumentException if the data stream is null, empty, or the POJO class is not annotated with @ExcelMapper.
     */
    public Workbook mapToExcel(Stream<?> dataStream) {

        // Create a new workbook for the Excel file
        Workbook workbook = new XSSFWorkbook();

        // Validate the input stream
        if (Objects.isNull(dataStream)) {
            throw new IllegalArgumentException("The list of data cannot be empty.");
        }

        List<?> dataList = dataStream.collect(Collectors.toList());
        // Get the class of the first object in the stream
        Class<?> clazz = extractClassFromStream(dataList);
        // Check if the class has the @ExcelMapper annotation
        ExcelMapper sheetAnnotation = clazz.getAnnotation(ExcelMapper.class);
        if (sheetAnnotation == null) {
            throw new IllegalArgumentException("POJO class must have an @ExcelMapper annotation.");
        }

        // Retrieve the sheet name from the annotation
        String sheetName = sheetAnnotation.sheetName();
        // Create a new sheet in the workbook with the provided name
        Sheet sheet = workbook.createSheet(sheetName);

        // Step 1: Extract Column Metadata
        ColumnMetadataExtractor columnMetadataExtractor = new ColumnMetadataExtractor();
        List<ColumnMetadata> columnMetadataList = columnMetadataExtractor.process(clazz);

        // Step 2: Populate header values
        populateHeaderValues(sheet, columnMetadataList);

        // Step 3: Populate data rows
        populateRowValues(dataList.stream(), sheet, columnMetadataList);

        // Return the generated workbook
        return workbook;
    }

    private Class<?> extractClassFromStream(List<?> dataStream) {
        // Peek the first element from the stream to infer the class
        return dataStream.stream().findFirst()
                .map(Object::getClass)
                .orElseThrow(() -> new IllegalArgumentException("The data stream is empty."));
    }

    /**
     * Populates the header row of the Excel sheet with column names.
     * The column headers are derived from the column metadata and styled accordingly.
     *
     * @param sheet              the sheet in the workbook where headers are to be populated.
     * @param columnMetadataList the list of ColumnMetadata to determine the headers.
     */
    private void populateHeaderValues(Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        // Create the first row for the headers
        Row headerRow = sheet.createRow(Config.ROW_VALUE_FOR_HEADER);
        // Set the header cell style
        CellStyle cellStyle = CellUtils.styleForHeader(sheet);

        // Populate each header cell
        for (ColumnMetadata columnMetadata : columnMetadataList) {
            // Create a cell for each column and set the header value
            Cell cell = headerRow.createCell(columnMetadata.getColumnOrder() - 1);
            cell.setCellValue(columnMetadata.getHeader());
            // Apply the header style to the cell
            cell.setCellStyle(cellStyle);
        }

        // Auto-size the columns to fit the content
        CellUtils.autoSize(sheet, columnMetadataList);
    }

    /**
     * Populates the data rows of the Excel sheet with the values from the POJO stream.
     * The values are extracted based on the column metadata and set in the corresponding cells.
     *
     * @param dataStream         the stream of POJOs from which data values will be extracted.
     * @param sheet              the sheet in the workbook where the data rows are to be populated.
     * @param columnMetadataList the list of ColumnMetadata to determine the column order and corresponding data.
     */
    private void populateRowValues(Stream<?> dataStream, Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        // Atomic integer to keep track of the row index
        AtomicInteger rowIndex = new AtomicInteger(Config.ROW_VALUE_START_FOR_DATA);

        // Process each object in the stream
        dataStream.forEach(targetObject -> {
            // Create a new row for the current object
            Row row = sheet.createRow(rowIndex.getAndIncrement());

            // Iterate over the column metadata and set values for each column
            for (ColumnMetadata columnMetadata : columnMetadataList) {
                // Extract the field value using the FieldExtractor
                String value = fieldExtractor.process(columnMetadata, targetObject);

                // Create a cell for each column and set the extracted value
                Cell cell = row.createCell(columnMetadata.getColumnOrder() - 1);
                cell.setCellValue(value);
            }
        });
    }
}
