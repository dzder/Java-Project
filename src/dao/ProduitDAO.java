package dao;

import model.Produit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exception.*;

public class ProduitDAO {

    // 1. LIRE TOUS LES PRODUITS
    public List<Produit> lireTous() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(new Produit(
                    rs.getInt("idProduit"),
                    rs.getString("nomProduit"),
                    rs.getDouble("prix"),
                    rs.getInt("quantite"),
                    rs.getInt("seuilMinimal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    // 2. AJOUTER UN NOUVEAU PRODUIT
    public void ajouter(Produit p) {
        String sql = "INSERT INTO produit (nomProduit, prix, quantite, seuilMinimal) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNomProduit());
            pstmt.setDouble(2, p.getPrix());
            pstmt.setInt(3, p.getQuantite());
            pstmt.setInt(4, p.getSeuilMinimal());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. TROUVER PAR ID (Utile pour les ventes)
    public Produit trouverParId(int id) throws EntityNotFoundException {
        String sql = "SELECT * FROM produit WHERE idProduit = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite"),
                        rs.getInt("seuilMinimal")
                    );
                } else {
                    // Au lieu de retourner null, on lance l'exception
                    throw new EntityNotFoundException("Le produit avec l'ID " + id + " n'existe pas.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL", e);
        }
    }

    // 4. METTRE À JOUR LE STOCK (Utilisé après une vente ou un achat)
    // Utilisez une valeur négative pour diminuer le stock, positive pour l'augmenter
    public void modifierStock(int idProduit, int variation) throws StockInsuffisantException, SQLException {
    // 1. On vérifie d'abord le stock actuel
    try {
        Produit p = trouverParId(idProduit);
        if ( (p.getQuantite() + variation) < 0) {
            throw new StockInsuffisantException("Action impossible : Stock insuffisant pour " + p.getNomProduit());
        }
    } catch (EntityNotFoundException e) {
        throw new RuntimeException("Erreur lors de la vérification du stock", e);
    }

    String sql = "UPDATE produit SET quantite = quantite + ? WHERE idProduit = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, variation);
        pstmt.setInt(2, idProduit);
        pstmt.executeUpdate();
    } 
    // On ne fait plus e.printStackTrace() ici, on laisse l'erreur remonter au Menu
}

    // 5. LIRE LES ALERTES (Stock critique)
    public List<Produit> lireAlertes() {
        List<Produit> alertes = new ArrayList<>();
        // On récupère les produits où la quantité est <= au seuil
        String sql = "SELECT * FROM produit WHERE quantite <= seuilMinimal";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alertes.add(new Produit(
                    rs.getInt("idProduit"),
                    rs.getString("nomProduit"),
                    rs.getDouble("prix"),
                    rs.getInt("quantite"),
                    rs.getInt("seuilMinimal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alertes;
    }

    // 6. SUPPRIMER UN PRODUIT
    public void supprimer(int id) {
        String sql = "DELETE FROM produit WHERE idProduit = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}