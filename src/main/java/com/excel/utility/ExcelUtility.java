package com.excel.utility;

import com.excel.utility.extractor.FieldExtractor;
import com.excel.utility.processor.ObjectValueProcessor;
import com.excel.utility.processor.impl.DefaultObjectValueProcessor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.stream.Stream;

import static com.excel.utility.Config.DEFAULT_SHEET_NAME;

/**
 * The {@code ExcelUtility} class provides functionality to map a list or stream of Plain Old Java Objects (POJOs)
 * into an Excel workbook, facilitating data export from Java applications to Excel format.
 * It uses annotations to map fields in POJOs to columns in the Excel sheet, making the process simple and customizable.
 */
public class ExcelUtility {

    private final FieldExtractor fieldExtractor;
    private final ObjectValueProcessor objectValueProcessor;
    private final ExcelUtilityHelper excelUtilityHelper;

    /**
     * Default constructor initializing the utility with default implementations.
     */
    public ExcelUtility() {
        this(new FieldExtractor(), new DefaultObjectValueProcessor());
    }

    /**
     * Constructor for custom {@link FieldExtractor}.
     */
    public ExcelUtility(FieldExtractor fieldExtractor) {
        this(fieldExtractor, new DefaultObjectValueProcessor());
    }

    /**
     * Constructor for custom {@link ObjectValueProcessor}.
     */
    public ExcelUtility(ObjectValueProcessor objectValueProcessor) {
        this(new FieldExtractor(), objectValueProcessor);
    }

    /**
     * Constructor for custom {@link FieldExtractor} and {@link ObjectValueProcessor}.
     */
    public ExcelUtility(FieldExtractor fieldExtractor, ObjectValueProcessor objectValueProcessor) {
        this.fieldExtractor = fieldExtractor;
        this.objectValueProcessor = objectValueProcessor;
        this.excelUtilityHelper = new ExcelUtilityHelper(fieldExtractor, objectValueProcessor);
    }

    /**
     * Generic method to map a stream of data to a Workbook.
     */
    private <T extends Workbook> T mapToWorkbook(Stream<?> dataStream, String sheetName, T workbook) {
        return excelUtilityHelper.mapToWorkbook(dataStream, sheetName, workbook);
    }

    /**
     * Methods for mapping List data to various Workbook types.
     */
    public XSSFWorkbook mapToXSSFWorkbook(List<?> dataList) {
        return mapToWorkbook(dataList.stream(), DEFAULT_SHEET_NAME, new XSSFWorkbook());
    }

    public XSSFWorkbook mapToXSSFWorkbook(List<?> dataList, String sheetName) {
        return mapToWorkbook(dataList.stream(), sheetName, new XSSFWorkbook());
    }

    public SXSSFWorkbook mapToSXSSFWorkbook(List<?> dataList) {
        return mapToWorkbook(dataList.stream(), DEFAULT_SHEET_NAME, new SXSSFWorkbook());
    }

    public SXSSFWorkbook mapToSXSSFWorkbook(List<?> dataList, String sheetName) {
        return mapToWorkbook(dataList.stream(), sheetName, new SXSSFWorkbook());
    }

    public HSSFWorkbook mapToHSSFWorkbook(List<?> dataList) {
        return mapToWorkbook(dataList.stream(), DEFAULT_SHEET_NAME, new HSSFWorkbook());
    }

    public HSSFWorkbook mapToHSSFWorkbook(List<?> dataList, String sheetName) {
        return mapToWorkbook(dataList.stream(), sheetName, new HSSFWorkbook());
    }

    /**
     * Methods for mapping Stream data to various Workbook types.
     */
    public XSSFWorkbook mapToXSSFWorkbook(Stream<?> dataStream) {
        return mapToWorkbook(dataStream, DEFAULT_SHEET_NAME, new XSSFWorkbook());
    }

    public XSSFWorkbook mapToXSSFWorkbook(Stream<?> dataStream, String sheetName) {
        return mapToWorkbook(dataStream, sheetName, new XSSFWorkbook());
    }

    public SXSSFWorkbook mapToSXSSFWorkbook(Stream<?> dataStream) {
        return mapToWorkbook(dataStream, DEFAULT_SHEET_NAME, new SXSSFWorkbook());
    }

    public SXSSFWorkbook mapToSXSSFWorkbook(Stream<?> dataStream, String sheetName) {
        return mapToWorkbook(dataStream, sheetName, new SXSSFWorkbook());
    }

    public HSSFWorkbook mapToHSSFWorkbook(Stream<?> dataStream) {
        return mapToWorkbook(dataStream, DEFAULT_SHEET_NAME, new HSSFWorkbook());
    }

    public HSSFWorkbook mapToHSSFWorkbook(Stream<?> dataStream, String sheetName) {
        return mapToWorkbook(dataStream, sheetName, new HSSFWorkbook());
    }

    /**
     * Methods for appending data to existing Workbooks.
     */
    public Workbook mapToExistingWorkbook(Workbook workbook, List<?> dataList, String sheetName) {
        return mapToWorkbook(dataList.stream(), sheetName, workbook);
    }

    public Workbook mapToExistingWorkbook(Workbook workbook, Stream<?> dataStream, String sheetName) {
        return mapToWorkbook(dataStream, sheetName, workbook);
    }
}
