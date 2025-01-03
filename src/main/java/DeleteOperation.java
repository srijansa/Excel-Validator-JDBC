import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DeleteOperation {
    public void delteOperation() {
        System.out.println("Choose from the following options:");
        System.out.println("\n1.Delete by ID \n2.Delete by Name \n3.Delete by email");
        System.out.println("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                    System.out.println("Enter ID: ");
                    int id = scanner.nextInt();
                    deleteById(id);
                    break;
            case 2:
                    System.out.println("Enter Name: ");
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    deleteByName(name);
                    break;
            case 3:
                    System.out.println("Enter email: ");
                    scanner.nextLine();
                    String email = scanner.nextLine();
                    deleteByEmail(email);
                    break;
            default:
                    System.out.println("Invalid option.");
        }
    }

    public void deleteById(int id){
        String query = "DELETE from users where id = ?";
        try{
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            int result = pstmt.executeUpdate();
            if (result == 1){
                System.out.println("User deleted successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByName(String name) {
        String query = "DELETE from users where name = ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Deleted "+ result+" records successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void deleteByEmail(String email) {
        String query = "DELETE from users where email = ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("Deleted "+ result+" records successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}