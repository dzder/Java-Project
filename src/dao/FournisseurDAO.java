package dao;

import model.Fournisseur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO {

    public List<Fournisseur> lireTous() {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT * FROM fournisseur";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fournisseurs.add(new Fournisseur(
                    rs.getInt("idFour"),
                    rs.getString("nom"),
                    rs.getString("contact")
                    
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return fournisseurs;
    }

    public void ajouter(Fournisseur f) {
        String sql = "INSERT INTO fournisseur (nomFournisseur, contact) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, f.getNom());
            pstmt.setString(2, f.getContact());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}