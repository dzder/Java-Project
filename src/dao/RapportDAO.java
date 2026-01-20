package dao;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class RapportDAO {

    // 1. État des stocks : Produits où quantite <= seuilMinimal
    public void remplirTableauStocks(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT nomProduit, quantite, seuilMinimal FROM produit WHERE quantite <= seuilMinimal * 1.2";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int qte = rs.getInt("quantite");
                int seuil = rs.getInt("seuilMinimal");
                String etat = (qte <= seuil) ? "CRITIQUE" : "ATTENTION";
                model.addRow(new Object[]{rs.getString("nomProduit"), qte, seuil, etat});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 2. Chiffre d'Affaires (par jour)
    public void remplirTableauCA(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT DATE(dateVente) as jour, COUNT(idVente) as nb, SUM(montantVente) as total " +
                     "FROM vente GROUP BY DATE(dateVente) ORDER BY jour DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getDate("jour"), rs.getInt("nb"), rs.getDouble("total")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 3. Performance Fournisseurs
    public void remplirTableauFournisseurs(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT f.nom, COUNT(c.idCommande) as nbCmd, SUM(conc.montant) as depense " +
                     "FROM fournisseur f LEFT JOIN commandeFournisseur c ON f.idFour = c.idFour LEFT JOIN concerne conc ON c.idCommande = conc.idCommande " +
                     "WHERE c.statut = 'recu' GROUP BY f.idFour";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("nom"), rs.getInt("nbCmd"), rs.getDouble("depense")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public void remplirHistoriqueAchats(DefaultTableModel model, String emailClient) {
    // On vide le tableau avant de le remplir
    model.setRowCount(0);

    // Requête SQL complexe pour lier les ventes aux produits et au client
    String sql = "SELECT v.dateVente, p.nomProduit, co.quantite, co.montant " +
                 "FROM vente v " +
                 "JOIN client cl ON v.idClient = cl.idClient " +
                 "JOIN correspond co ON v.idVente = co.idVente " +
                 "JOIN produit p ON co.idProduit = p.idProduit " +
                 "WHERE cl.mail = ? " +
                 "ORDER BY v.dateVente DESC";

    try (Connection conn = Database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, emailClient);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            // Récupération des données
            Object[] row = {
                rs.getTimestamp("dateVente"),
                rs.getString("nomProduit"),
                rs.getInt("quantite"),
                String.format("%.2f €", rs.getDouble("montant"))
            };
            // Ajout de la ligne au modèle de la JTable
            model.addRow(row);
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        e.printStackTrace();
    }
}
}