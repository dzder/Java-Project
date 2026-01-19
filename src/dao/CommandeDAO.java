package dao;

import java.sql.*;

public class CommandeDAO {

    // 1. Créer une nouvelle commande
    public void creerCommande(int idFour, double montant) {
        // Correction : nom de la table 'commandeFournisseur' et colonne 'idFour'
        String sql = "INSERT INTO commandeFournisseur (dateCommande, statut, idFour) VALUES (NOW(), 'en_attente', ?)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFour);
            pstmt.executeUpdate();
            System.out.println("Succès : Commande créée dans 'commandeFournisseur'.");
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL (creerCommande) : " + e.getMessage());
        }
    }

    // 2. Valider la réception : Met à jour le statut ET le stock produit
    public boolean validerReception(int idCommande, int idProduit, int quantiteRecue) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Début de la transaction

            // A. Mise à jour du statut dans commandeFournisseur
            String sqlStatut = "UPDATE commandeFournisseur SET statut = 'recu' WHERE idCommande = ?";
            PreparedStatement pstmtStatut = conn.prepareStatement(sqlStatut);
            pstmtStatut.setInt(1, idCommande);
            int resStatut = pstmtStatut.executeUpdate();

            // B. Mise à jour du stock dans la table produit
            String sqlStock = "UPDATE produit SET quantite = quantite + ? WHERE idProduit = ?";
            PreparedStatement pstmtStock = conn.prepareStatement(sqlStock);
            pstmtStock.setInt(1, quantiteRecue);
            pstmtStock.setInt(2, idProduit);
            int resStock = pstmtStock.executeUpdate();

            // Vérification que les deux updates ont touché une ligne
            if (resStatut > 0 && resStock > 0) {
                conn.commit();
                System.out.println("Stock mis à jour et commande marquée comme 'recu'.");
                return true;
            } else {
                conn.rollback();
                System.err.println("Échec : Commande ou Produit introuvable. Annulation.");
                return false;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Erreur lors de la réception : " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}