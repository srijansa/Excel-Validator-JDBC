import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InsertOperation {
    public void insertSingle(String name, String email, String address, String password) throws SQLException{
        String query = "INSERT INTO users (name, email, address, password) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, address);
            ps.setString(4, password);
            ps.executeUpdate();
            System.out.println("Single Record ---> Inserted successfully");
        }
    }

    public void insertFromExcel(String excelFilePath){
        String query = "INSERT INTO users (name, email, address, password) VALUES (?, ?, ?, ?)";

        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis);
             Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String address = row.getCell(2).getStringCellValue();
                String password = row.getCell(3).getStringCellValue();

                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, address);
                pstmt.setString(4, password);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Data Inserted ---> from Excel.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
