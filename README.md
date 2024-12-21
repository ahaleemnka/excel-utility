
# Excel Mapping Utility

This application provides a utility to map a list of POJOs (Plain Old Java Objects) to an Excel workbook, creating well-organized and metadata-driven Excel sheets. It uses Java reflection and annotations to automatically extract column metadata and populate Excel sheets with headers and data rows. 

## Key Features:
- **Excel Mapping:** Map a list of POJOs to an Excel workbook with dynamically populated columns and data.
- **Annotations-Based Metadata Extraction:** Use custom annotations (`@ExcelSheet` and `@ExcelColumn`) to define the structure of the Excel sheet (such as sheet name, column names, and orders).
- **Metadata Validation:** Ensure that column orders are unique, valid, and do not exceed the predefined limit.
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
- [Contributing](#contributing)
- [License](#license)

---

## Installation

1. Clone this repository to your local machine.
   ```bash
   git clone https://github.com/your-repo/excel-mapper.git
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
@ExcelSheet(sheetName = "Employee Data")
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
        ExcelMapper excelMapper = new ExcelMapper();
        Workbook workbook = excelMapper.mapToExcel(employees);

        // Now you can write the workbook to a file or use it further
    }
}
```

### Excel Output
This will produce an Excel sheet named **"Employee Data"** with three columns: **Employee ID**, **Employee Name**, and **Is Active**.

---

## Class Definitions

### `ExcelMapper`

- **Purpose:** The `ExcelMapper` class converts a list of POJOs into an Excel workbook. It processes annotations like `@ExcelSheet` and `@ExcelColumn` to structure the sheet and columns.
- **Key Methods:**
  - `mapToExcel`: Maps the POJO list to an Excel workbook.
  - `populateHeaderValues`: Populates the header row with column names.
  - `populateRowValues`: Populates the data rows using the field values extracted from POJOs.

### `ColumnMetadataExtractor`

- **Purpose:** Extracts column metadata from POJOs, including field names, annotations, column orders, and headers.
- **Key Methods:**
  - `process`: Extracts metadata from the POJO class and its fields, ensuring correct column orders and headers.
  - `extractColumnMetadata`: Recursively extracts metadata, detecting circular references.
  - `assignMissingColumnOrders`: Assigns missing column orders for fields that do not have a predefined order.

### `HeaderNameProcessUtils`

- **Purpose:** A utility class that processes and generates Excel column headers.
- **Key Methods:**
  - `convertHeader`: Converts field names to human-readable header format.
  - `convertFieldNameToHeader`: Converts a field name to a proper header format by handling camelCase, underscores, dashes, and numbers.

### `ValidationUtils`

- **Purpose:** Validates column metadata, ensuring column orders are valid and unique.
- **Key Methods:**
  - `validateMetadata`: Validates the metadata list, ensuring no invalid or duplicate column orders.
  - `validateColumnOrder`: Ensures that the column order is valid (greater than 0 and less than a maximum value).
  - `checkForDuplicateColumnOrder`: Checks for duplicate column orders and throws an exception if found.

### `ValueFlattenProcessor`

- **Purpose:** Flattens complex data structures like lists and maps into string representations suitable for Excel cells.
- **Key Methods:**
  - `flattenList`: Flattens a list of objects into a comma-separated string.
  - `flattenMap`: Flattens a map into a colon-separated key-value string.

### `FieldExtractor`

- **Purpose:** Extracts field values from a POJO instance using reflection, based on column metadata.
- **Key Methods:**
  - `process`: Extracts the value for a given field from a POJO object.

---

## Testing

### Unit Test Cases

The application includes comprehensive unit tests to ensure the functionality works correctly. Test cases cover scenarios such as:

- **POJO Mapping to Excel**: Verifying that data from POJOs is correctly mapped to Excel columns.
- **Column Metadata Extraction**: Ensuring that the `ColumnMetadataExtractor` handles field annotations and metadata extraction properly.
- **Header Conversion**: Ensuring correct header formatting for different field names and camelCase.
- **Metadata Validation**: Checking that column orders are unique and within valid ranges.
- **Circular Reference Handling**: Ensuring that circular references in POJOs are detected and handled properly.

The tests provide coverage for edge cases and ensure that the logic works across a variety of scenarios.

```java
@Test
void testProcess_ValidColumnMetadata() {
    // Test the successful extraction of column metadata from a POJO class
    ...
}

@Test
void testProcess_WithCircularReference() {
    // Test that circular references in classes are detected
    ...
}

@Test
void testProcess_FieldNotFound() {
    // Test case where a field is not found in the class
    ...
}
```

You can run the unit tests using the following Maven or Gradle command:
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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
