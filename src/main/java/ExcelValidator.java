import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;
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
        switch (primaryValidator.toLowerCase()){
            case "email": return EMAIL_PATTERN.matcher(value).matches();
            case "numeric": return NUMERIC_PATTERN.matcher(value).matches();
            case "alphanumeric": return ALPHANUMERIC_PATTERN.matcher(value).matches();
            case "nonempty": return !value.trim().isEmpty();
            case "date": return value.matches("^\\d{4}-\\d{2}-\\d{2}$");  // Date format supported: YYYY-MM-DD
            default: throw new IllegalArgumentException("Invalid primary validator: " + primaryValidator);
        }
    }
}
