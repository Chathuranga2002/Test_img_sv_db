import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRetriever {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test4";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        // Replace '1' with the actual image ID and provide a custom folder path
        retrieveImageFromDatabase(4, "outImg", "custom_image5.jpg");
    }

    public static void retrieveImageFromDatabase(int imageId, String folderPath, String outputFileName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT image_data FROM images WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, imageId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        InputStream imageStream = resultSet.getBinaryStream("image_data");
                        String outputPath = folderPath + File.separator + outputFileName;

                        // Create the folder if it doesn't exist
                        File folder = new File(folderPath);
                        folder.mkdirs();

                        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = imageStream.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                            System.out.println("Image retrieved and saved to " + outputPath);
                        }
                    } else {
                        System.out.println("Image not found for ID: " + imageId);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
