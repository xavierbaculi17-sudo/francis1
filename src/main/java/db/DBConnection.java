package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles the SQLite connection and first-run setup of students.db. */
public class DBConnection {

    private static final String URL = "jdbc:sqlite:students.db";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the users table and seeds sample accounts when the table is empty. */
    public static void initialize() {
        String create = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "roles TEXT NOT NULL, "
                + "status TEXT NOT NULL)";
        String count = "SELECT COUNT(*) FROM users";
        String insert = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.execute(create);
            }

            int rows = 0;
            try (PreparedStatement ps = conn.prepareStatement(count);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rows = rs.getInt(1);
                }
            }

            if (rows == 0) {
                String[][] seed = {
                        {"admin",    "admin123", "ADMIN",   "APPROVED"},
                        {"student1", "pass123",  "STUDENT", "PENDING"},
                        {"student2", "pass123",  "STUDENT", "APPROVED"},
                        {"teacher1", "pass123",  "TEACHER", "APPROVED"}
                };
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    for (String[] u : seed) {
                        ps.setString(1, u[0]);
                        ps.setString(2, u[1]);
                        ps.setString(3, u[2]);
                        ps.setString(4, u[3]);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }
}
