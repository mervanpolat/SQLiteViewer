package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.Vector;

public class SQLiteViewer extends JFrame {

    private JComboBox<String> tablesComboBox;
    private JTextArea queryTextArea;
    private JButton executeQueryButton;
    private JTable table;
    private JTextField fileNameTextField;

    public SQLiteViewer() {
        // Set the title of the window
        setTitle("SQLite Viewer");

        // Initialize components
        fileNameTextField = new JTextField();
        JButton openFileButton = new JButton("Open");
        tablesComboBox = new JComboBox<>();
        queryTextArea = new JTextArea();
        executeQueryButton = new JButton("Execute");
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Set the names of the components
        fileNameTextField.setName("FileNameTextField");
        openFileButton.setName("OpenFileButton");
        tablesComboBox.setName("TablesComboBox");
        queryTextArea.setName("QueryTextArea");
        executeQueryButton.setName("ExecuteQueryButton");
        table.setName("Table");

        // Set layout and add components
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(fileNameTextField);
        topPanel.add(openFileButton);
        topPanel.add(tablesComboBox);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(queryTextArea), BorderLayout.CENTER);
        add(executeQueryButton, BorderLayout.SOUTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Set default close operation and size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);

        // Start with disabled components until a valid database is loaded
        disableComponents();

        // Action listener for the 'Open' button
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String databaseFileName = fileNameTextField.getText();
                if (!databaseFileName.isEmpty()) {
                    connectToDatabase(databaseFileName);
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "Please enter a database file name.");
                    disableComponents();
                }
            }
        });

        // Action listener for the ComboBox to update the query text area
        tablesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTable = (String) tablesComboBox.getSelectedItem();
                if (selectedTable != null) {
                    queryTextArea.setText("SELECT * FROM " + selectedTable + ";");
                    queryTextArea.setEnabled(true);
                    executeQueryButton.setEnabled(true);
                }
            }
        });

        // Action listener for the 'Execute' button
        executeQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = queryTextArea.getText();
                if (!query.isEmpty()) {
                    executeQuery(query);
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "Please enter an SQL query.");
                }
            }
        });

        // Make the window visible
        setVisible(true);
    }

    private void connectToDatabase(String databaseFileName) {
        String url = "jdbc:sqlite:" + databaseFileName;
        File dbFile = new File(databaseFileName);

        if (!dbFile.exists()) {
            JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
            disableComponents();
            return;
        }

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet tables = meta.getTables(null, null, null, new String[]{"TABLE"});

                tablesComboBox.removeAllItems();

                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    if (!tableName.startsWith("sqlite_")) {
                        tablesComboBox.addItem(tableName);
                    }
                }
                enableComponents();
            }
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(new Frame(), "Cannot connect to the database!");
            disableComponents();
            throwables.printStackTrace();
        }
    }

    private void executeQuery(String query) {
        String databaseFileName = fileNameTextField.getText();
        String url = "jdbc:sqlite:" + databaseFileName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();

            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();

            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);

        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(new Frame(), "SQL error: " + throwables.getMessage());
            throwables.printStackTrace();
            // Optionally, add error handling and display a message to the user
        }
    }

    private void disableComponents() {
        executeQueryButton.setEnabled(false);
        queryTextArea.setEnabled(false);
        // Do not disable tablesComboBox
    }

    private void enableComponents() {
        executeQueryButton.setEnabled(true);
        queryTextArea.setEnabled(true);
        tablesComboBox.setEnabled(true); // Ensure this is enabled when a valid connection is established
    }

}

