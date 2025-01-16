# Excel Mapping Utility

This application provides a utility to map a list of POJOs (Plain Old Java Objects) to an Excel workbook, creating well-organized and metadata-driven Excel sheets. It uses Java reflection and annotations to automatically extract column metadata and populate Excel sheets with headers and data rows.

See medium publish for the same:\
[Part 1 - The journey to simplifying excel handling](https://medium.com/@ahaleemnka/part-1-the-journey-to-simplifying-excel-handling-4d910a59e0e0)  
[Part 2 - Mastering-the-excel-mapper-utility](https://medium.com/@ahaleemnka/part-2-mastering-the-excel-mapper-utility-5006484b6b1f)  
[Part 3 - Setting-up-and-using-the-excel-mapper-utility](https://medium.com/@ahaleemnka/part-3-setting-up-and-using-the-excel-mapper-utility-16b06433a979)

## Key Features:
- **Excel Mapping:** Map a list of POJOs to an Excel workbook with dynamically populated columns and data.
- **Annotations-Based Metadata Extraction:** Use custom annotations (`@ExcelMapper` and `@ExcelColumn`) to define the structure of the Excel sheet (such as sheet name, column names, and orders).
- **Stream Support:** Map Java streams to Excel seamlessly, improving performance and flexibility.
- **Append Data:** Append rows to existing Excel sheets.
- **Metadata Validation:** Ensure column orders are unique, valid, and do not exceed the predefined limit.
- **Data Flattening:** Flatten lists and maps to store in Excel cells in a readable format.
- **Circular Reference Detection:** Avoid infinite recursion during metadata extraction through circular reference detection.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Class Definitions](#class-definitions)
  - [ExcelMapper](#excelmapper)
  - [ColumnMetadataExtractor](#columnmetadataextractor)
  - [HeaderNameProcessUtils](#headernameprocessutils)
  - [ValidationUtils](#validationutils)
  - [ValueFlattenProcessor](#valueflattenprocessor)
  - [FieldExtractor](#fieldextractor)
- [Testing](#testing)
  - [Unit Test Cases](#unit-test-cases)
- [Changelog](#changelog)
- [Contributing](#contributing)
- [License](#license)

---

## Installation

1. Clone this repository to your local machine.
   ```bash
   git clone https://github.com/ahaleemnka/excel-utility.git
   cd excel-mapper
   ```

2. Build the project using Maven or Gradle (depending on your build tool).

   For Maven:
   ```bash
   mvn clean install
   ```

   For Gradle:
   ```bash
   gradle build
   ```

---

## Usage

### Example POJO Class with Annotations
```java
@ExcelMapper
public class Employee {
  @ExcelColumn(header = "Employee ID", columnOrder = 1)
  private int employeeId;

  @ExcelColumn(header = "Employee Name", columnOrder = 2)
  private String employeeName;

  @ExcelColumn(header = "Is Active", columnOrder = 3)
  private boolean employeeActive;

  // Getters and Setters
}
```

### Mapping to Excel
```java
public class Main {
  public static void main(String[] args) {
    List<Employee> employees = // Get your employee data list
            ExcelUtility excelUtility = new ExcelUtility();
    Workbook workbook = excelUtility.mapToWorkbook(employees, "Employee Data");

    // Now you can write the workbook to a file or use it further
  }
}
```

### Appending Data
```java
public class Main {
  public static void main(String[] args) {
    List<Employee> newEmployees = // New employee data
            Workbook existingWorkbook = // Load existing workbook

            ExcelUtility excelUtility = new ExcelUtility();
    excelUtility.appendToWorkbook(existingWorkbook, newEmployees, "Employee Data");

    // Save or use the updated workbook
  }
}
```

---

## Class Definitions

### `ExcelUtility`

- **Purpose:** The `ExcelUtility` class converts a list or stream of POJOs into an Excel workbook. It processes annotations like `@ExcelMapper` and `@ExcelColumn` to structure the sheet and columns.
- **Key Methods:**
  - `mapToWorkbook`: Maps a POJO list or stream to an Excel workbook.
  - `appendToWorkbook`: Appends data rows to an existing sheet.
  - `mapToHSSFWorkbook` and `mapToXSSFWorkbook`: Provide specific workbook formats.

### `ColumnMetadataExtractor`

- **Purpose:** Extracts column metadata from POJOs, including field names, annotations, column orders, and headers.
- **Key Methods:**
  - `process`: Extracts metadata from the POJO class and its fields, ensuring correct column orders and headers.
  - `extractColumnMetadata`: Recursively extracts metadata, detecting circular references.

### `HeaderNameProcessUtils`

- **Purpose:** A utility class that processes and generates Excel column headers.
- **Key Methods:**
  - `convertHeader`: Converts field names to human-readable header format.

### `ValidationUtils`

- **Purpose:** Validates column metadata, ensuring column orders are valid and unique.

### `ValueFlattenProcessor`

- **Purpose:** Flattens complex data structures like lists and maps into string representations suitable for Excel cells.
- **Key Methods:**
  - `flattenList`: Flattens a list of objects into a comma-separated string.
  - `flattenMap`: Flattens a map into a colon-separated key-value string.

### `FieldExtractor`

- **Purpose:** Extracts field values from a POJO instance using reflection, based on column metadata.

---

## Testing

### Unit Test Cases

The application includes comprehensive unit tests to ensure the functionality works correctly. Test cases cover scenarios such as:

- **POJO Mapping to Excel:** Verifying that data from POJOs is correctly mapped to Excel columns.
- **Column Metadata Extraction:** Ensuring that the `ColumnMetadataExtractor` handles field annotations and metadata extraction properly.
- **Header Conversion:** Ensuring correct header formatting for different field names.
- **Metadata Validation:** Checking that column orders are unique and within valid ranges.
- **Stream Handling:** Validating the mapping of streams to workbooks.

Run the unit tests using Maven or Gradle:
```bash
mvn test
```
or
```bash
gradle test
```

---

## Contributing

We welcome contributions to improve the application. If you'd like to contribute, please follow these steps:

1. Fork this repository.
2. Create a new branch for your feature or bugfix.
3. Write tests for your changes.
4. Submit a pull request with a description of your changes.

---

## Changelog
### Version 2.0.0 (2025-01-15)
- **Enhancement:** Consolidated logic for mapping `Stream` or `List` data into a single method for simplicity.
- **Feature Update:** Added support for appending data to existing workbooks.
- **Optimization:** Improved metadata validation and streamlined processing.

### Version 1.4.0 (2025-01-14)
- **Enhancement**: Improved handling of Excel sheet names.
    - Sheet names now support special characters and invalid characters are replaced with dashes.
    - Leading and trailing whitespace in sheet names is automatically trimmed.
    - Sheet names exceeding 31 characters are truncated to comply with Excel's limitations.
- **Feature Update**: Removed the `sheetName` attribute from the `@ExcelMapper` annotation. The sheet name is now provided as a parameter to the `ExcelUtility.mapToExcel` method.
- **Bug Fixes**: Improved handling of metadata processing and data mapping.

### Version 1.3.0 (2025-01-05)
- **Enhancement**: Improved processing of lists and maps for Excel output.
    - Lists are now converted to comma-separated strings for better readability in Excel cells.
    - Maps are now converted to key-value pairs formatted as "key : value", with each pair on a new line.
- **Feature Update**: Updated `ValueFlattenProcessor` to handle new list and map formatting requirements.
- **Feature Update**: If the nested object class is not annotated with `@ExcelMapper`, the nested object is converted to a string and its fields are not processed. To include nested object fields, annotate the class with `@ExcelMapper`.
- **Feature Update**: Added support for streams. Now, streams can also be provided instead of just lists.
- **Bug Fixes**: Various bug fixes, including improved handling of nested classes and exception handling for invalid annotations.

### Version 1.1.0 (2025-01-01)
- **New Feature**: Introduced `includeAll` feature for `@ExcelMapper` annotation.
    - If `includeAll` is set to `true`, all non-annotated fields will also be included in metadata processing.
    - If `includeAll` is set to `false`, only fields annotated with `@ExcelColumn` will be included in metadata processing.
- **Improvement**: Enhanced metadata processing for nested classes.
    - When `includeAll` is set to `true`, fields of nested classes will be included in the metadata list.
    - Nested classes that are not annotated with `@ExcelMapper` are now processed correctly when `includeAll` is enabled.
- Bug Fix: Fixed exception handling for missing `@ExcelMapper` annotation on classes.

### Version 1.0.0 (2024-12-01)
- Initial release with core functionality for processing Excel metadata using annotations `@ExcelMapper` and `@ExcelColumn`.

---

## License

This project is licensed under the Apache License 2.0 License - see the [LICENSE](LICENSE) file for details.

---