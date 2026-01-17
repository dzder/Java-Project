package model;

public class Concerne {
    private int idCommande;
    private int idProduit;
    private int quantite;
    private double montant;

    public Concerne() {}

    public Concerne(int idCommande, int idProduit, int quantite, double montant) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.montant = montant;
    }

    public int getIdCommande() { return idCommande; }
    public int getIdProduit() { return idProduit; }
    public int getQuantite() { return quantite; }
    public double getMontant() { return montant; }

    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setMontant(double montant) { this.montant = montant; }
}
