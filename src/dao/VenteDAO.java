package dao;

import model.Vente;
import model.Correspond;
import java.sql.*;
import java.util.List;

public class VenteDAO {

    // Cette méthode enregistre une vente complète avec ses produits
    public boolean enregistrerVente(Vente vente, List<Correspond> produitsVendus) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            // On désactive l'auto-commit pour gérer la transaction manuellement
            conn.setAutoCommit(false);

            // 1. Insérer la vente principale
            String sqlVente = "INSERT INTO vente (montantVente, dateVente, idUtil, idClient) VALUES (?, ?, ?, ?)";
            // RETURN_GENERATED_KEYS permet de récupérer l'ID (idVente) qui vient d'être créé
            PreparedStatement pstmtVente = conn.prepareStatement(sqlVente, Statement.RETURN_GENERATED_KEYS);
            pstmtVente.setDouble(1, vente.getMontantVente());
            // Conversion de java.util.Date en java.sql.Date
            pstmtVente.setDate(2, new java.sql.Date(vente.getDateVente().getTime()));
            pstmtVente.setInt(3, vente.getIdUtil());
            pstmtVente.setInt(4, vente.getIdClient());
            pstmtVente.executeUpdate();

            // Récupérer l'idVente généré
            ResultSet rs = pstmtVente.getGeneratedKeys();
            int idVenteGenere = 0;
            if (rs.next()) {
                idVenteGenere = rs.getInt(1);
            }

            // 2. Insérer les détails dans la table 'correspond' et mettre à jour le stock
            String sqlCorrespond = "INSERT INTO correspond (idVente, idProduit, quantite, montant) VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE produit SET quantite = quantite - ? WHERE idProduit = ?";
            
            PreparedStatement pstmtCorr = conn.prepareStatement(sqlCorrespond);
            PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock);

            for (Correspond item : produitsVendus) {
                // Remplissage de la table de liaison
                pstmtCorr.setInt(1, idVenteGenere);
                pstmtCorr.setInt(2, item.getIdProduit());
                pstmtCorr.setInt(3, item.getQuantite());
                pstmtCorr.setDouble(4, item.getMontant());
                pstmtCorr.executeUpdate();

                // Mise à jour du stock produit
                pstmtStock.setInt(1, item.getQuantite());
                pstmtStock.setInt(2, item.getIdProduit());
                int rowsAffected = pstmtStock.executeUpdate();
                
                if (rowsAffected == 0) {
                    throw new SQLException("Échec mise à jour stock pour le produit ID: " + item.getIdProduit());
                }
            }

            // Si tout est OK, on valide la transaction
            conn.commit();
            System.out.println("Vente enregistrée avec succès !");
            return true;

        } catch (SQLException e) {
            // En cas d'erreur, on annule tout ce qui a été fait dans cette transaction
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction annulée : " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // On n'oublie pas de remettre l'auto-commit et de fermer
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}