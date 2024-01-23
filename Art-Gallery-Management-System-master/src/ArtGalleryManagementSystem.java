import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ArtGalleryManagementSystem extends JFrame {
    private DefaultTableModel artworkTableModel;
    private JTable artworkTable;
    private JLabel imageContainer;
    private JButton crossButton;
    private JButton deleteImageButton;

    // JDBC variables
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/image_gallery";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public ArtGalleryManagementSystem() {
        initUI();
        initDatabase();
    }

    private void initUI() {
        setTitle("Art Gallery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set background color to red
        getContentPane().setBackground(Color.RED);


        // Artwork Table
        artworkTableModel = new DefaultTableModel();
        artworkTable = new JTable(artworkTableModel);
        JScrollPane scrollPane = new JScrollPane(artworkTable);
        add(scrollPane, BorderLayout.CENTER);

        // Image Container
        imageContainer = new JLabel();
        add(imageContainer, BorderLayout.EAST);
        imageContainer.setBackground(Color.YELLOW);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Add Image Button
        JButton addImageButton = new JButton("Add Image");
        addImageButton.addActionListener(e -> handleAddImage());
        buttonPanel.add(addImageButton);


        // Listener to show image when a row in the table is selected
        artworkTable.getSelectionModel().addListSelectionListener(e -> showImage());

        // Initialize crossButton
        crossButton = new JButton("Close");
        crossButton.addActionListener(e -> handleClose());
        crossButton.setVisible(false); // Initially set to invisible
        add(crossButton, BorderLayout.SOUTH);

        // Delete Image Button
        deleteImageButton = new JButton("Delete Image");
        deleteImageButton.addActionListener(e -> handleDeleteImage());
//        deleteImageButton.setBounds(0,100,30,20);
        buttonPanel.add(deleteImageButton);

        setVisible(true);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void initDatabase() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM image_table");

            // Populate Artwork Table
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add columns to the table model
            for (int i = 1; i <= columnCount; i++) {
                artworkTableModel.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                artworkTableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleAddImage() {

        String imagePath = JOptionPane.showInputDialog(this, "Enter Image Path:");

        if (imagePath != null && !imagePath.isEmpty()) {

            addImagePathToDatabase(imagePath);


            refreshArtworkTable();
        }
    }
    private void handleDeleteImage() {
        // Ask for the ID of the image to delete
        String idToDelete = JOptionPane.showInputDialog(this, "Enter the ID of the image to delete:");

        if (idToDelete != null && !idToDelete.isEmpty()) {
            // Delete the image from the database and refresh the table
            deleteImageFromDatabase(Integer.parseInt(idToDelete));
            refreshArtworkTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleClose() {

        imageContainer.setIcon(null);
        crossButton.setVisible(false);

        artworkTable.setVisible(true);


        artworkTable.clearSelection();
    }

    private void addImagePathToDatabase(String imagePath) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            // Note: 'id' is excluded from the column list, as it is an auto-increment field
            String sql = "INSERT INTO image_table (ImagePath) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, imagePath);
                preparedStatement.executeUpdate();

                // Retrieve the auto-generated ID (if needed)
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Image path added with ID: " + generatedId);
                } else {
                    System.out.println("Image path added, but retrieving ID failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteImageFromDatabase(int imageId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM image_table WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, imageId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Image deleted from the database.");
                } else {
                    System.out.println("Image deletion failed. Image not found in the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshArtworkTable() {
        // Clear the current data in the table
        artworkTableModel.setRowCount(0);

        // Reload the data from the database
        initDatabase();
    }

    private void showImage() {
        int selectedRow = artworkTable.getSelectedRow();
        if (selectedRow != -1) {
            String imagePath = artworkTableModel.getValueAt(selectedRow, artworkTableModel.findColumn("ImagePath")).toString();

            try {
                // Load the image from the file path

                ImageIcon imageIcon = new ImageIcon(imagePath);
//                imageIcon.setImage(size());
//                imageContainer.setSize(800,600);
                // Set the image to the JLabel without resizing
                imageContainer.setIcon(imageIcon);

                // Show the close button
                crossButton.setVisible(true);

                artworkTable.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // No image selected, clear the imageContainer and hide the close button
            imageContainer.setIcon(null);
            crossButton.setVisible(false);
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(ArtGalleryManagementSystem::new);
    }
}
