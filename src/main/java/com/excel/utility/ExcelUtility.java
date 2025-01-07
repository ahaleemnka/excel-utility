package com.excel.utility;

import com.excel.utility.annotation.ExcelMapper;
import com.excel.utility.dto.ColumnMetadata;
import com.excel.utility.extractor.ColumnMetadataExtractor;
import com.excel.utility.extractor.FieldExtractor;
import com.excel.utility.processor.ObjectValueProcessor;
import com.excel.utility.processor.impl.DefaultObjectValueProcessor;
import com.excel.utility.util.CellUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code ExcelUtility} class provides functionality to map a list or stream of Plain Old Java Objects (POJOs)
 * into an Excel workbook, facilitating data export from Java applications to Excel format.
 * It uses annotations to map fields in POJOs to columns in the Excel sheet, making the process simple and customizable.
 *
 * <p>Key features of this utility include:</p>
 * <ul>
 *   <li>Annotation-based mapping of POJOs to Excel sheets and columns using the {@link ExcelMapper} annotation.</li>
 *   <li>Efficient handling of large datasets via Java streams, minimizing memory consumption.</li>
 *   <li>Customizable field extraction and value processing using pluggable components like {@link FieldExtractor} and {@link ObjectValueProcessor}.</li>
 * </ul>
 *
 * <p>This class requires that POJOs be annotated with {@link ExcelMapper} to provide sheet names and column metadata
 * for creating the Excel workbook. Additionally, headers and data rows are automatically styled for improved readability.</p>
 */
public class ExcelUtility {

    /**
     * Extractor responsible for retrieving field values from POJOs based on column metadata.
     */
    private final FieldExtractor fieldExtractor;

    /**
     * Processor responsible for converting object field values to their corresponding string representation for Excel cells.
     */
    private final ObjectValueProcessor objectValueProcessor;

    /**
     * Default constructor initializing the utility with default implementations for field extraction and value processing.
     */
    public ExcelUtility() {
        this.fieldExtractor = new FieldExtractor();
        this.objectValueProcessor = new DefaultObjectValueProcessor();
    }

    /**
     * Constructor initializing the utility with a custom {@link FieldExtractor}.
     *
     * @param fieldExtractor the custom field extractor to use.
     */
    public ExcelUtility(FieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
        this.objectValueProcessor = new DefaultObjectValueProcessor();
    }

    /**
     * Constructor initializing the utility with a custom {@link ObjectValueProcessor}.
     *
     * @param objectValueProcessor the custom value processor to use.
     */
    public ExcelUtility(ObjectValueProcessor objectValueProcessor) {
        this.fieldExtractor = new FieldExtractor();
        this.objectValueProcessor = objectValueProcessor;
    }

    /**
     * Constructor initializing the utility with custom implementations for both field extraction and value processing.
     *
     * @param fieldExtractor the custom field extractor to use.
     * @param objectValueProcessor the custom value processor to use.
     */
    public ExcelUtility(FieldExtractor fieldExtractor, ObjectValueProcessor objectValueProcessor) {
        this.fieldExtractor = fieldExtractor;
        this.objectValueProcessor = objectValueProcessor;
    }

    /**
     * Maps a list of POJOs to an Excel workbook.
     *
     * <p>This method collects the data from the provided list and creates an Excel sheet with the appropriate headers and rows.</p>
     *
     * @param dataList the list of POJOs to map to the Excel workbook.
     * @return a {@link Workbook} object containing the mapped data.
     * @throws IllegalArgumentException if the data list is empty or the POJO class is not annotated with {@link ExcelMapper}.
     */
    public Workbook mapToExcel(List<?> dataList) {
        return mapToExcel(dataList.stream());
    }

    /**
     * Maps a stream of POJOs to an Excel workbook.
     *
     * <p>This method efficiently handles large datasets by processing the stream incrementally and reducing memory usage.</p>
     *
     * @param dataStream the stream of POJOs to map to the Excel workbook.
     * @return a {@link Workbook} object containing the mapped data.
     * @throws IllegalArgumentException if the data stream is null, empty, or the POJO class is not annotated with {@link ExcelMapper}.
     */
    public Workbook mapToExcel(Stream<?> dataStream) {
        if (Objects.isNull(dataStream)) {
            throw new IllegalArgumentException("The data stream cannot be null.");
        }

        List<?> dataList = dataStream.collect(Collectors.toList());
        Class<?> clazz = extractClassFromStream(dataList);

        ExcelMapper sheetAnnotation = clazz.getAnnotation(ExcelMapper.class);
        if (sheetAnnotation == null) {
            throw new IllegalArgumentException("POJO class must be annotated with @ExcelMapper.");
        }

        String sheetName = sheetAnnotation.sheetName();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Extract column metadata for header and data population
        ColumnMetadataExtractor columnMetadataExtractor = new ColumnMetadataExtractor();
        List<ColumnMetadata> columnMetadataList = columnMetadataExtractor.process(clazz);

        // Populate header row and data rows based on column metadata
        populateHeaderValues(sheet, columnMetadataList);
        populateRowValues(dataList.stream(), sheet, columnMetadataList);

        return workbook;
    }

    /**
     * Extracts the class type from the first object in the provided data list.
     *
     * @param dataList the list of data objects to extract the class from.
     * @return the class type of the first object.
     * @throws IllegalArgumentException if the list is empty.
     */
    private Class<?> extractClassFromStream(List<?> dataList) {
        return dataList.stream()
                .findFirst()
                .map(Object::getClass)
                .orElseThrow(() -> new IllegalArgumentException("The data list is empty."));
    }

    /**
     * Populates the header row in the Excel sheet with the column names derived from the POJO's annotations.
     *
     * @param sheet the sheet in the workbook where the header should be populated.
     * @param columnMetadataList the list of {@link ColumnMetadata} objects containing column order and header names.
     */
    private void populateHeaderValues(Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        Row headerRow = sheet.createRow(Config.ROW_VALUE_FOR_HEADER);
        CellStyle cellStyle = CellUtils.styleForHeader(sheet);

        // Iterate through column metadata and set headers in the first row
        for (ColumnMetadata columnMetadata : columnMetadataList) {
            Cell cell = headerRow.createCell(columnMetadata.getColumnOrder() - 1);
            cell.setCellValue(columnMetadata.getHeader());
            cell.setCellStyle(cellStyle);
        }

        // Auto-size the columns based on the longest content
        CellUtils.autoSize(sheet, columnMetadataList);
    }

    /**
     * Populates the data rows in the Excel sheet based on the provided POJO stream.
     *
     * @param dataStream the stream of POJOs containing the data values for the rows.
     * @param sheet the sheet in the workbook where data rows should be populated.
     * @param columnMetadataList the list of {@link ColumnMetadata} objects containing column order and metadata.
     */
    private void populateRowValues(Stream<?> dataStream, Sheet sheet, List<ColumnMetadata> columnMetadataList) {
        AtomicInteger rowIndex = new AtomicInteger(Config.ROW_VALUE_START_FOR_DATA);

        // Iterate through each object in the stream and create corresponding rows in the sheet
        dataStream.forEach(targetObject -> {
            Row row = sheet.createRow(rowIndex.getAndIncrement());

            // Iterate through columns and set cell values based on POJO field values
            for (ColumnMetadata columnMetadata : columnMetadataList) {
                Object fieldValue = fieldExtractor.process(columnMetadata, targetObject);
                String value = objectValueProcessor.process(fieldValue);

                // Truncate long values to avoid Excel's character limit for a cell (32,767 characters)
                if (value != null && value.length() > 32767) {
                    value = value.substring(0, 32760);
                }

                Cell cell = row.createCell(columnMetadata.getColumnOrder() - 1);
                cell.setCellValue(value);
            }
        });
    }
}
