package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CommandeFournisseur;
import model.Concerne;

public class CommandeDAO {

    // 1. Lire toutes les commandes pour le tableau
    public List<CommandeFournisseur> lireToutes() {
        List<CommandeFournisseur> liste = new ArrayList<>();
        // On fait une jointure pour avoir le nom du fournisseur au lieu de juste l'ID
        String sql = "SELECT c.*, f.nom FROM commandeFournisseur c " +
                     "JOIN fournisseur f ON c.idFour = f.idFour ORDER BY c.dateCommande DESC";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CommandeFournisseur cf = new CommandeFournisseur();
                cf.setIdCommande(rs.getInt("idCommande"));
                cf.setIdFour(rs.getInt("idFour"));
                cf.setDateCmd(rs.getDate("dateCommande"));
                cf.setStatut(rs.getString("statut"));
                
                // Le montant peut être calculé ou stocké, ici on suppose une colonne montant
                // Si vous n'avez pas de colonne montant, il faudra faire une somme sur la table 'concerne'
                
                liste.add(cf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // 2. Méthode appelée par le Panel pour la réception globale
    public void receptionner(int idCmd) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // A. Mettre à jour le statut
            String sqlStatut = "UPDATE commandeFournisseur SET statut = 'recu' WHERE idCommande = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStatut)) {
                ps.setInt(1, idCmd);
                ps.executeUpdate();
            }

            // B. Récupérer tous les produits liés à cette commande via la table 'concerne'
            String sqlItems = "SELECT idProduit, quantite FROM concerne WHERE idCommande = ?";
            try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                psItems.setInt(1, idCmd);
                ResultSet rs = psItems.executeQuery();

                // C. Pour chaque produit, augmenter le stock
                String sqlUpdateStock = "UPDATE produit SET quantite = quantite + ? WHERE idProduit = ?";
                try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                    while (rs.next()) {
                        psStock.setInt(1, rs.getInt("quantite"));
                        psStock.setInt(2, rs.getInt("idProduit"));
                        psStock.executeUpdate();
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    // 3. Créer une nouvelle commande vide
        public void creerCommande(int idFour) {
            String sql = "INSERT INTO commandeFournisseur (dateCommande, statut, idFour, montantTotal) VALUES (NOW(), 'en_attente', ?, 0)";
            try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idFour);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void creerCommandeComplete(int idFour, List<Concerne> items) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // 1. Insérer la commande
            String sqlCmd = "INSERT INTO commandeFournisseur (dateCommande, statut, idFour) VALUES (NOW(), 'en_attente', ?)";
            int idGenere = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idFour);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idGenere = rs.getInt(1);
            }

            // 2. Insérer les lignes dans 'concerne'
            String sqlItem = "INSERT INTO concerne (idCommande, idProduit, quantite, montant) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                for (Concerne item : items) {
                    ps.setInt(1, idGenere);
                    ps.setInt(2, item.getIdProduit());
                    ps.setInt(3, item.getQuantite());
                    ps.setDouble(4, item.getMontant());
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }
}