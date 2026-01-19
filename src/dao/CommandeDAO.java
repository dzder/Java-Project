package dao;

import java.sql.*;

public class CommandeDAO {

    // 1. Créer une nouvelle commande (Sans idUtil)
    public void creerCommande(int idFour) {
        // La requête SQL correspond maintenant strictement à votre CREATE TABLE
        String sql = "INSERT INTO commandeFournisseur (dateCommande, statut, idFour) VALUES (NOW(), 'en_attente', ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFour);
            pstmt.executeUpdate();
            System.out.println("Succès : Commande fournisseur créée.");
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la création de commande : " + e.getMessage());
        }
    }

    // 2. Valider la réception (Met à jour le statut et augmente le stock)
    public boolean validerReception(int idCommande, int idProduit, int quantiteRecue) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Sécurité : Tout ou rien

            // A. Marquer la commande comme 'recu'
            String sqlStatut = "UPDATE commandeFournisseur SET statut = 'recu' WHERE idCommande = ?";
            PreparedStatement pstmtStatut = conn.prepareStatement(sqlStatut);
            pstmtStatut.setInt(1, idCommande);
            int rowsStatut = pstmtStatut.executeUpdate();

            // B. Ajouter la quantité au stock du produit
            String sqlStock = "UPDATE produit SET quantite = quantite + ? WHERE idProduit = ?";
            PreparedStatement pstmtStock = conn.prepareStatement(sqlStock);
            pstmtStock.setInt(1, quantiteRecue);
            pstmtStock.setInt(2, idProduit);
            int rowsStock = pstmtStock.executeUpdate();

            if (rowsStatut > 0 && rowsStock > 0) {
                conn.commit();
                System.out.println("Réception validée : Stock mis à jour !");
                return true;
            } else {
                conn.rollback();
                System.err.println("Erreur : Commande ou Produit introuvable.");
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}