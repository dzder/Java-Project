import dao.*;

import model.*;


import java.util.ArrayList;
import java.util.List;


import java.util.Date;

//this is the main class to test the DAO functionalities
public class Main {
    public static void main(String[] args) {
        testerApprovisionnement();
       /*  // ======= Test ProduitDAO avant vente =======
        ProduitDAO produitDAO = new ProduitDAO();

        // 1. Lire tous les produits
        System.out.println("\n=== Tous les produits ===");
        List<Produit> allProduits = produitDAO.lireTous();
        for (Produit p : allProduits) {
            System.out.println("ID: " + p.getIdProduit() + " | " + p.getNomProduit() + " | Prix: " + p.getPrix() + "€ | Stock: " + p.getQuantite());
        }
         // ======= Test VenteDAO =======
        System.out.println("\n\n========== TEST VENTEDAO ==========\n");
        VenteDAO venteDAO = new VenteDAO();

        // Créer une nouvelle vente
        System.out.println("=== Enregistrement d'une vente ===");
        Vente newVente = new Vente(0, 0, new Date(), 1, 1); // idUtil=1, idClient=1
        
        // Créer les produits vendus (avec quantités et montants)
        List<Correspond> produitsVendus = new ArrayList<>();
        produitsVendus.add(new Correspond(0, 1, 2, 10.00));  // Produit ID 1: 2 unités à 10€
        produitsVendus.add(new Correspond(0, 2, 1, 15.50));  // Produit ID 2: 1 unité à 15.50€
        produitsVendus.add(new Correspond(0, 3, 3, 25.00));  // Produit ID 3: 3 unités à 25€
        
        // Calculer le montant total
        double montantTotal = 0;
        for (Correspond c : produitsVendus) {
            montantTotal += c.getMontant();
        }
        newVente.setMontantVente(montantTotal);
        
        System.out.println("Montant total de la vente: " + montantTotal + "€");
        System.out.println("Nombre de produits: " + produitsVendus.size());
        
        // Enregistrer la vente
        boolean success = venteDAO.enregistrerVente(newVente, produitsVendus);
        if (success) {
            System.out.println("✓ Vente enregistrée avec succès !");
        } else {
            System.out.println("✗ Erreur lors de l'enregistrement de la vente");
        }

        
         
                // ======= Test ProduitDAO apres vente =======
        

        // 1. Lire tous les produits
        System.out.println("\n=== Tous les produits ===");
        List<Produit> allProduits1 = produitDAO.lireTous();
        for (Produit p : allProduits1) {
            System.out.println("ID: " + p.getIdProduit() + " | " + p.getNomProduit() + " | Prix: " + p.getPrix() + "€ | Stock: " + p.getQuantite());
        }

        
                // ======= Test ClientDAO =======*/
        /*ClientDAO clientDAO = new ClientDAO();

        // 1. Ajouter un client
        System.out.println("\n=== Ajout de clients ===");
        Client newClient = new Client(0, "Bowie David", "David@gmail.com");
        if(clientDAO.trouverParMail(newClient.getMail()) != null) {
            System.out.println("Le client avec le mail " + newClient.getMail() + " existe déjà.");
        } else{
            clientDAO.ajouterClient(newClient);}

        // 2. Lire tous les clients
        System.out.println("\n=== Tous les clients ===");
        List<Client> allClients = clientDAO.lireTous();
        for (Client c : allClients) {
            System.out.println("ID: " + c.getIdClient() + " | Nom: " + c.getNomPrenom() + " | Mail: " + c.getMail());
        }

        // 3. Trouver un client par mail
        System.out.println("\n=== Recherche par mail ===");
        Client found = clientDAO.trouverParMail("David@gmail.com");
        if (found != null) {
            System.out.println("Client trouvé: " + found.getNomPrenom() + " (" + found.getMail() + ")");
        } else {
            System.out.println("Client non trouvé");
        }*/ 
            }
        public static void testerApprovisionnement() {
        FournisseurDAO fDao = new FournisseurDAO();
        CommandeDAO cDao = new CommandeDAO();
        ProduitDAO pDao = new ProduitDAO();

        System.out.println("=== TEST RÉAPPROVISIONNEMENT ===");

        // 1. Créer et ajouter un fournisseur
        Fournisseur nouveauFou = new Fournisseur(0, "PharmaPlus", "0102030405");
        fDao.ajouter(nouveauFou);
        System.out.println("1. Fournisseur 'PharmaPlus' ajouté.");

        // 2. Vérifier un produit avant la commande (ex: ID 1)
        Produit pInitial = pDao.trouverParId(1);
        if (pInitial == null) {
            System.out.println("Erreur : Le produit avec l'ID 1 n'existe pas dans la base.");
            return;
        }
        int stockAvant = pInitial.getQuantite();
        System.out.println("2. Stock actuel du produit '" + pInitial.getNomProduit() + "' : " + stockAvant);

        // 3. Créer une commande (idFournisseur: 1, idUtilisateur: 1, montant: 500.0)
        // Note : Assurez-vous que l'utilisateur 1 existe dans votre table utilisateur
        cDao.creerCommande(1, 1, 500.0);
        System.out.println("3. Commande créée en statut 'en_attente'.");

        // 4. Simuler la réception (idCommande: 1, idProduit: 1, quantité: 50)
        System.out.println("4. Validation de la réception (Arrivée de 50 unités)...");
        boolean succes = cDao.validerReception(1, 1, 50);

        if (succes) {
            // 5. Vérifier si le stock a bien augmenté
            Produit pApres = pDao.trouverParId(1);
            System.out.println("5. Succès ! Nouveau stock : " + pApres.getQuantite() + " (Ancien : " + stockAvant + ")");
        } else {
            System.out.println("Échec de la validation.");
        }
    }
}
