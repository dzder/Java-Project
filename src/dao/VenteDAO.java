package dao;

import model.Vente;
import model.Correspond;
import model.Produit;
import exception.StockInsuffisantException;
import exception.EntityNotFoundException; // Ne pas oublier cet import
import java.sql.*;
import java.util.List;

public class VenteDAO {

    public boolean enregistrerVente(Vente vente, List<Correspond> produitsVendus) throws StockInsuffisantException, SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false); // Début de la transaction ACID

            ProduitDAO pDao = new ProduitDAO();

            // 1. VÉRIFICATION DES STOCKS AVANT TOUTE OPÉRATION
            for (Correspond item : produitsVendus) {
                try {
                    Produit p = pDao.trouverParId(item.getIdProduit());
                    if (p.getQuantite() < item.getQuantite()) {
                        throw new StockInsuffisantException("Vente annulée : Stock insuffisant pour " + p.getNomProduit() 
                                                            + " (Dispo: " + p.getQuantite() + ")");
                    }
                } catch (EntityNotFoundException e) {
                    throw new StockInsuffisantException("Vente annulée : ID Produit " + item.getIdProduit() + " introuvable.");
                }
            }

            // 2. INSERTION DE LA VENTE PRINCIPALE
            String sqlVente = "INSERT INTO vente (montantVente, dateVente, idUtil, idClient) VALUES (?, ?, ?, ?)";
            int idVenteGenere = 0;
            try (PreparedStatement pstmtVente = conn.prepareStatement(sqlVente, Statement.RETURN_GENERATED_KEYS)) {
                pstmtVente.setDouble(1, vente.getMontantVente());
                pstmtVente.setDate(2, new java.sql.Date(vente.getDateVente().getTime()));
                pstmtVente.setInt(3, vente.getIdUtil());
                pstmtVente.setInt(4, vente.getIdClient());
                pstmtVente.executeUpdate();

                ResultSet rs = pstmtVente.getGeneratedKeys();
                if (rs.next()) {
                    idVenteGenere = rs.getInt(1);
                }
            }

            // 3. INSERTION DANS 'CORRESPOND' ET MISE À JOUR DU STOCK
            String sqlCorr = "INSERT INTO correspond (idVente, idProduit, quantite, montant) VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE produit SET quantite = quantite - ? WHERE idProduit = ?";

            try (PreparedStatement pstmtCorr = conn.prepareStatement(sqlCorr);
                 PreparedStatement pstmtStock = conn.prepareStatement(sqlUpdateStock)) {

                for (Correspond item : produitsVendus) {
                    // Insertion détail
                    pstmtCorr.setInt(1, idVenteGenere);
                    pstmtCorr.setInt(2, item.getIdProduit());
                    pstmtCorr.setInt(3, item.getQuantite());
                    pstmtCorr.setDouble(4, item.getMontant());
                    pstmtCorr.executeUpdate();

                    // Mise à jour stock
                    pstmtStock.setInt(1, item.getQuantite());
                    pstmtStock.setInt(2, item.getIdProduit());
                    pstmtStock.executeUpdate();
                }
            }

            conn.commit(); // Tout est OK, on valide
            return true;

        } catch (SQLException | StockInsuffisantException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e; // On propage l'erreur pour l'interface graphique
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}