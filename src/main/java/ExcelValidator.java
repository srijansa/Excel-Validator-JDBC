import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Pattern;

public class ExcelValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[a-zA-Z]{2,4}$");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9]*$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]");

    // Mapping the names of the header to their indices
    private Map<String, Integer> getColumnIndexMap(Row headerRow){
        Map<String, Integer> colIndexMap = new HashMap<String, Integer>();
        for (Cell cell : headerRow) {
            colIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return colIndexMap;
    }

    // Checking for primary validators
    private boolean validatePrimary(String value, String primaryValidator) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        switch (primaryValidator.toLowerCase()){
            case "email": return EMAIL_PATTERN.matcher(value).matches();
            case "numeric": return NUMERIC_PATTERN.matcher(value).matches();
            case "alphanumeric": return ALPHANUMERIC_PATTERN.matcher(value).matches();
            case "nonempty": return !value.trim().isEmpty();
            case "date": return value.matches("^\\d{4}-\\d{2}-\\d{2}$");  // Date format supported: YYYY-MM-DD
            default: throw new IllegalArgumentException("Invalid primary validator: " + primaryValidator);
        }
    }

    // Validating using secondary validators
    private boolean validateSecondary(String value, String secondaryValidator) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        if (secondaryValidator.startsWith("length(")){
            String[] bounds = secondaryValidator.substring(7, secondaryValidator.length() - 1).split(",");
            int minLength = Integer.parseInt(bounds[0]);
            int maxLength = Integer.parseInt(bounds[1]);
            return value.length() >= minLength && value.length() <= maxLength;
        }else if (secondaryValidator.startsWith("startswith(")){
            String bounds = Arrays.toString(secondaryValidator.substring(11, secondaryValidator.length() - 1).split(","));
            return value.startsWith(bounds);
        } else if (secondaryValidator.startsWith("contains(")){
            String bounds = Arrays.toString(secondaryValidator.substring(9, secondaryValidator.length() - 1).split(","));
            return value.contains(bounds);
        } else if (secondaryValidator.startsWith("regex(")){
            String pattern = Arrays.toString(secondaryValidator.substring(6, secondaryValidator.length() - 1).split(","));
            return value.matches(pattern);
        } else {
            throw new IllegalArgumentException("Invalid secondary validator: " + secondaryValidator);
        }
    }

    private String getCellValue(Cell cell){
        if (cell == null) return null;
        switch (cell.getCellType()){
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: throw new IllegalArgumentException("Invalid cell type: " + cell.getCellType());
        }
    }

    public void printInvalidData(Map<Integer, List<String>> invalidData){
        for (Map.Entry<Integer, List<String>> entry : invalidData.entrySet()) {
            int rowNumber = entry.getKey();
            List<String> invalidValues = entry.getValue();
            System.out.println("Row " + rowNumber + ": " + invalidValues);
            for (String iValue : invalidValues) {
                System.out.println("  " + iValue);
            }
        }
    }

    public Map<Integer, List<String>> validate(String excelPath, Map<String, String> primaryValidators, Map<String, List<String>> secondaryValidators) {
        Map<Integer, List<String>> invalidData = new HashMap<>();
        try{
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndexMap = getColumnIndexMap(headerRow);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                List<String> iValid = new ArrayList<>();
                for (Map.Entry<String, String> entry: primaryValidators.entrySet()){
                    String columnName = entry.getKey();
                    String primaryValidator = entry.getValue();
                    if (!columnIndexMap.containsKey(columnName)) {
                        iValid.add("Column "+columnName+" not found");
                        continue;
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
