# SQLite Viewer

## About

SQLite Viewer is a Java-based desktop application that provides a user-friendly interface for interacting with SQLite databases. It allows users to connect to a local SQLite database file, browse tables, and execute SQL queries against those tables. The application is designed for those who need a simple and efficient way to view and manipulate SQLite databases without using command-line tools or writing code.

## Features

- **Open Database Files**: Users can open local SQLite database files by entering the file name.
- **Browse Tables**: Once connected, the tables within the database are listed, and users can select them from a drop-down menu.
- **Execute Queries**: Users can execute custom SQL queries by entering them into a text area and running them against the selected database.
- **Error Handling**: The application provides informative error messages if the file does not exist or if there is an error with the SQL query.
- **Dynamic Table View**: Query results are displayed in a dynamic table view, allowing users to see the data returned from executed queries.

## How to Use

1. **Start the Application**: Run the `SQLiteViewer` class. The application window will open with the interface for interaction.

2. **Load a Database File**: Enter the path to a local SQLite database file in the text field and click the "Open" button. If the file exists and is a valid SQLite database, the tables within the database will be loaded into the `TablesComboBox`.

3. **Browse Tables**: Select a table from the `TablesComboBox` to auto-generate a "SELECT *" SQL query in the `QueryTextArea`.

4. **Custom Queries**: Optionally, edit the query in the `QueryTextArea` or enter a new SQL query to be executed.

5. **Execute Queries**: Click the "Execute" button to run the SQL query. The results will be displayed in the table view in the main area of the application.

6. **Error Messages**: If an error occurs (e.g., file not found, SQL error), a dialog box will display the relevant error message.

## Example Scenario

**John, a database administrator, needs to quickly view the contents of a `users` table in an SQLite database file named `company.db`.**

1. John opens the SQLite Viewer application.
2. He enters the path to `company.db` in the file name text field and clicks "Open".
3. The application lists the available tables in the `TablesComboBox`. John selects the `users` table.
4. The `QueryTextArea` auto-populates with `SELECT * FROM users;`.
5. John clicks "Execute", and the application displays the contents of the `users` table in the table view.

**In the event that John mistypes the file name as `comapny.db`:**

1. Upon clicking "Open", an error dialog pops up, stating "File doesn't exist!".
2. John corrects the file name to `company.db` and clicks "Open" again.
3. This time the application loads the tables successfully, and he proceeds with his task.

---

**Note**: To run the application, you will need Java installed on your system and have the SQLite JDBC driver included in your project's classpath.
