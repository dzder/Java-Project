package dao;

import model.Vente;
import model.Correspond;
import model.Produit;
import exception.StockInsuffisantException;
import java.sql.*;
import java.util.List;

public class VenteDAO {

    // Cette méthode enregistre une vente complète avec ses produits
    public boolean enregistrerVente(Vente vente, List<Correspond> produitsVendus) throws StockInsuffisantException, SQLException {
    Connection conn = null;
    try {
        conn = Database.getConnection();
        conn.setAutoCommit(false); // Début transaction

        // 1. VÉRIFICATION DES STOCKS AVANT TOUTE OPÉRATION
        ProduitDAO pDao = new ProduitDAO();
        for (Correspond item : produitsVendus) {
            Produit p = pDao.trouverParId(item.getIdProduit());
            if (p == null || p.getQuantite() < item.getQuantite()) {
                // Si un produit manque, on jette l'exception et on arrête tout
                throw new StockInsuffisantException("Vente annulée : Pas assez de " + 
                                                   (p != null ? p.getNomProduit() : "Produit inconnu"));
            }
        }

        // 2. INSERTION VENTE (votre code existant)
        // ... (insert vente, récup keys) ...

        // 3. INSERTION CORRESPOND ET UPDATE STOCK
        // ... (votre code existant) ...

        conn.commit();
        return true;

    } catch (SQLException | StockInsuffisantException e) {
        if (conn != null) conn.rollback();
        // On relance l'exception pour que le Menu sache quoi afficher
        throw e; 
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}
}