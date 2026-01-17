package app;

import model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ======= Test Utilisateur =======
        Utilisateur admin = new Utilisateur(1, "admin1", "mdp123", "admin");
        Utilisateur employe = new Utilisateur(2, "user1", "mdp456", "employe");

        System.out.println("admin est admin ? " + admin.isAdmin());      // true
        System.out.println("employe est admin ? " + employe.isAdmin());  // false

        // ======= Test Client =======
        Client client1 = new Client(1, "Bush Kate", "Kate@mail.com");
        Client client2 = new Client(2, "Nicks Stevie", "Stevie@mail.com");

        System.out.println("Client1 : " + client1.getNomPrenom() + " - " + client1.getMail());

        // ======= Test Produit =======
        Produit paracetamol = new Produit(1, "Paracetamol", 2.5, 50, 10);
        Produit ibuprofen = new Produit(2, "Ibuprofen", 5.0, 5, 10); // quantité critique

        System.out.println(paracetamol.getNomProduit() + " stock critique ? " + paracetamol.stockCritique());
        System.out.println(ibuprofen.getNomProduit() + " stock critique ? " + ibuprofen.stockCritique());

        // ======= Test CommandeFournisseur =======
        CommandeFournisseur cmd1 = new CommandeFournisseur(1, new Date(), "en_attente", 101);
        System.out.println("Commande " + cmd1.getIdCommande() + " statut : " + cmd1.getStatut());

        // ======= Test Vente =======
        Vente vente1 = new Vente(1, 15.0, new Date(), admin.getIdUtil(), client1.getIdClient());
        System.out.println("Vente " + vente1.getIdVente() + " montant : " + vente1.getMontantVente());

        // ======= Test Correspond (ligne de vente) =======
        Correspond ligne1 = new Correspond(vente1.getIdVente(), paracetamol.getIdProduit(), 3, 7.5);
        System.out.println("Ligne Vente : produit " + ligne1.getIdProduit() + ", quantité " + ligne1.getQuantite() + ", montant " + ligne1.getMontant());

        // ======= Test Concerne (ligne commande) =======
        Concerne ligneCmd = new Concerne(cmd1.getIdCommande(), ibuprofen.getIdProduit(), 10, 50.0);
        System.out.println("Ligne Commande : produit " + ligneCmd.getIdProduit() + ", quantité " + ligneCmd.getQuantite() + ", montant " + ligneCmd.getMontant());

        // ======= Liste d'objets =======
        List<Produit> produits = new ArrayList<>();
        produits.add(paracetamol);
        produits.add(ibuprofen);

        System.out.println("\n=== Produits stock critique ===");
        for (Produit p : produits) {
            if (p.stockCritique()) {
                System.out.println(p.getNomProduit() + " : " + p.getQuantite() + " restant");
            }
        }
    }
}
