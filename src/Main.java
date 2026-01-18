import dao.ClientDAO;
import dao.ProduitDAO;
import model.Produit;
import java.util.List;
import dao.ClientDAO;
import model.Client;
public class Main {
    public static void main(String[] args) {
                // ======= Test ProduitDAO =======
        ProduitDAO produitDAO = new ProduitDAO();

        // 1. Lire tous les produits
        System.out.println("\n=== Tous les produits ===");
        List<Produit> allProduits = produitDAO.lireTous();
        for (Produit p : allProduits) {
            System.out.println("ID: " + p.getIdProduit() + " | " + p.getNomProduit() + " | Prix: " + p.getPrix() + "€ | Stock: " + p.getQuantite());
        }

        // 2. Ajouter un nouveau produit
        System.out.println("\n=== Ajout d'un produit ===");
        Produit newProduit = new Produit(0, "Aspirine", 3.50, 100, 15);
        produitDAO.ajouter(newProduit);
        System.out.println("Aspirine ajoutée!");

        // 3. Trouver un produit par ID
        System.out.println("\n=== Recherche par ID ===");
        Produit found = produitDAO.trouverParId(1);
        if (found != null) {
            System.out.println("Produit trouvé: " + found.getNomProduit());
        }

        // 4. Afficher les alertes (stock critique)
        System.out.println("\n=== Produits en stock critique ===");
        List<Produit> alertes = produitDAO.lireAlertes();
        for (Produit p : alertes) {
            System.out.println(p.getNomProduit() + " : " + p.getQuantite() + " restant (seuil: " + p.getSeuilMinimal() + ")");
        }

        // 5. Modifier le stock (exemple: vente de 5 unités du produit 1)
        System.out.println("\n=== Modification du stock ===");
        produitDAO.modifierStock(1, -5);
        System.out.println("Stock du produit 1 diminué de 5 unités");
                // ======= Test ClientDAO =======
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
}