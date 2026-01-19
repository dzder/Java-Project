import dao.*;

import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.AuthentificationException;
import exception.StockInsuffisantException;

import java.util.Date;

//this is the main class to test the DAO functionalities
public class Main {
    public static void main(String[] args) throws AuthentificationException {
        //testerGestionFournisseurEtCommande();
        //testerProcessusVente();
        testerAuthentification();
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
        /*public static void testerApprovisionnement() {
        FournisseurDAO fDao = new FournisseurDAO();
        CommandeDAO cDao = new CommandeDAO();
        ProduitDAO pDao = new ProduitDAO();

        
    } */
    public static void testerGestionFournisseurEtCommande() {
        FournisseurDAO fDao = new FournisseurDAO();
        CommandeDAO cDao = new CommandeDAO();
        ProduitDAO pDao = new ProduitDAO();

        System.out.println("=== DEBUT DU TEST APPROVISIONNEMENT ===");

        // 1. TEST FOURNISSEUR
        // On crée un fournisseur (id 0 car auto-incrémenté)
        Fournisseur nouveauFou = new Fournisseur(0, "PharmaGrossiste", "Contact@pharma.com");
        fDao.ajouter(nouveauFou);
        
        // On vérifie s'il est bien en base
        System.out.println("Liste des fournisseurs en base :");
        for(Fournisseur f : fDao.lireTous()) {
            System.out.println("- ID: " + f.getIdFour() + " | Nom: " + f.getNom());
        }

        // 2. TEST COMMANDE (On utilise l'ID 1 pour le test)
        System.out.println("\n--- Création d'une commande ---");
        cDao.creerCommande(1); // On commande au fournisseur n°1

        // 3. TEST RÉCEPTION & STOCK
        // Imaginons que le produit n°1 (Doliprane) a 10 unités en stock
        Produit p = pDao.trouverParId(1);
        if (p != null) {
            System.out.println("Stock avant réception de " + p.getNomProduit() + " : " + p.getQuantite());
            
            // On valide la réception de la commande n°1 pour le produit n°1 (quantité +50)
            System.out.println("Validation de la réception de 50 unités...");
            boolean succes = cDao.validerReception(1, 1, 50);

            if (succes) {
                Produit pApres = pDao.trouverParId(1);
                System.out.println("Nouveau stock après réception : " + pApres.getQuantite());
            }
        } else {
            System.out.println("Erreur : Le produit ID 1 n'existe pas pour le test.");
        }

        System.out.println("=== FIN DU TEST ===");
    }
public static void testerProcessusVente() {
        VenteDAO venteDAO = new VenteDAO();
        ProduitDAO produitDAO = new ProduitDAO();
        
        System.out.println("=== DEBUT DU TEST DE VENTE ===");

        try {
            // 1. Préparation des données de la vente
            // Imaginons : Vente faite par l'Utilisateur 1 au Client 1
            int idUser = 1;
            int idClient = 1;
            
            // 2. Création du panier (Liste de produits à vendre)
            List<Correspond> panier = new ArrayList<>();
            
            // Produit 1 : On en veut 2 unités (Vérifiez que l'ID 1 existe en base)
            panier.add(new Correspond(0, 1, 2, 20.0)); 
            
            // Produit 2 : On en veut 1 unité (Vérifiez que l'ID 2 existe en base)
            panier.add(new Correspond(0, 2, 49, 15.5));

            // 3. Calcul du montant total
            double total = 0;
            for(Correspond c : panier) total += c.getMontant();

            // 4. Création de l'objet Vente
            Vente nouvelleVente = new Vente(0, total, new Date(), idUser, idClient);

            // 5. Tentative d'enregistrement
            System.out.println("Tentative d'enregistrement de la vente...");
            boolean succes = venteDAO.enregistrerVente(nouvelleVente, panier);

            if (succes) {
                System.out.println("✅ Vente réussie et stock mis à jour !");
            }

        } catch (StockInsuffisantException e) {
            // C'est ici que votre exception personnalisée est rattrapée
            System.err.println("❌ ECHEC DE VENTE : " + e.getMessage());
        } catch (Exception e) {
            // Pour toutes les autres erreurs (SQL, Connexion, etc.)
            System.err.println("⚠️ ERREUR TECHNIQUE : " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN DU TEST ===");
    }
    public static void testerAuthentification() {
        UtilisateurDAO uDao = new UtilisateurDAO();
        System.out.println("=== TEST AUTHENTIFICATION CORRIGÉ ===");

        try {
            // Test avec les noms de colonnes désormais corrects dans le DAO
            Utilisateur user = uDao.authentifier("admin1", "mdp_hash_admin"); 
            System.out.println("✅ Succès ! Connecté en tant que : " + user.getUsername());
        } catch (AuthentificationException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("⚠️ Erreur : " + e.getMessage());
        }
    }
}
