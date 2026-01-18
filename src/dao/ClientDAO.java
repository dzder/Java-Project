package dao;

import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    // Comme lireTous de ProduitDAO
    public List<Client> lireTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("idClient"), rs.getString("nomPrenom"), rs.getString("mail")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clients;
    }

    // NOUVEAU : Ajouter un client
    public void ajouterClient(Client c) {
        String sql = "INSERT INTO client (nomPrenom, mail) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getNomPrenom());
            pstmt.setString(2, c.getMail());
            pstmt.executeUpdate();
            System.out.println("Client ajouté avec succès !");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public Client trouverParMail(String mail) {
    String sql = "SELECT * FROM client WHERE mail = ?";
    Client client = null;

    try (Connection conn = Database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, mail); // On remplace le '?' par le mail recherché
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Si on trouve un résultat, on crée l'objet Client
                client = new Client(
                    rs.getInt("idClient"),
                    rs.getString("nomPrenom"),
                    rs.getString("mail")
                );
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la recherche du client : " + e.getMessage());
    }
    return client; // Retourne l'objet trouvé ou null s'il n'existe pas
}

}