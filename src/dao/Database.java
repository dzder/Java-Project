package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // Configuration basée sur votre script.sql
    private static final String URL = "jdbc:mysql://localhost:3306/java-project";
    private static final String USER = "java_user";
    private static final String PASSWORD = "placeholder"; // Assurez-vous que le mdp correspond

    public static Connection getConnection() throws SQLException {
        try {
            // Chargement du driver (nécessaire pour VS Code avec le .jar ajouté)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Pilote MySQL non trouvé. Vérifiez vos 'Referenced Libraries'.");
        }
    }
}