import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UpdateOperation {
    public void update(int userId){
        System.out.println("UpdateOperation");
        System.out.println("Check from the following: \n 1.Update Name \n 2.Update Email \n 3.Update Address 4.Change Password \nEnter from above: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        try {
            switch (option) {
                case 1:
                    updateName(userId);
                    break;
                case 2:
                    updateEmail(userId);
                    break;
                case 3:
                    updateAddress(userId);
                    break;
                case 4:
                    changePassword(userId);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateName(int userId) throws SQLException {
        String query = "UPDATE users SET name = ? WHERE id = ?";
        System.out.println("Enter the name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ){
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, userId);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Name updated successfully for userId"+userId);
            }
        }
    }
    public void updateEmail(int userId) {
        String query = "UPDATE users SET email = ? WHERE id = ?";
        System.out.println("Enter the email:");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setInt(2, userId);
            int result = pstmt.executeUpdate();
            if (result == 1) {
                System.out.println("Email updated successfully for userId"+ userId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAddress(int userId){
        String query = "UPDATE users SET address = ? WHERE id = ?";
    }
    public void changePassword(int userId){
        String query = "UPDATE users SET password = ? WHERE id = ?";
    }
}
