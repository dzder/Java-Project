package dao;

import model.Utilisateur;
import exception.AuthentificationException;
import java.sql.*;

public class UtilisateurDAO {

    public Utilisateur authentifier(String username, String password) throws AuthentificationException {
        // CORRECTION : Utilisation de 'username' et 'mdp' selon votre SQL
        String sql = "SELECT * FROM utilisateur WHERE username = ? AND mdp = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                        rs.getInt("idUtil"),
                        rs.getString("username"), // Correction
                        rs.getString("mdp"),      // Correction
                        rs.getString("role")
                    );
                } else {
                    throw new AuthentificationException("Nom d'utilisateur ou mot de passe incorrect.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de l'authentification : " + e.getMessage(), e);
        }
    }

    public void ajouter(Utilisateur u) {
        // CORRECTION : Noms des colonnes pour l'insertion
        String sql = "INSERT INTO utilisateur (username, mdp, role) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getMdp()); 
            pstmt.setString(3, u.getRole());
            pstmt.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout : " + e.getMessage());
        }
    }
}