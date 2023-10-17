import java.io.FileInputStream;
        import java.io.IOException;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.PreparedStatement;
        import java.sql.SQLException;

public class ImageSaver {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test4";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        String imagePath = "C:\\Users\\thisara\\Downloads\\image-removebg-preview (20).png"; // Replace with the actual image path

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertQuery = "INSERT INTO images (image_data) VALUES (?)";
            try (FileInputStream fis = new FileInputStream(imagePath);
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setBinaryStream(1, fis, fis.available());
                preparedStatement.executeUpdate();
                System.out.println("Image saved to the database.");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
