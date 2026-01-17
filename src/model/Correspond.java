package model;

public class Correspond {
    private int idVente;
    private int idProduit;
    private int quantite;
    private double montant;

    public Correspond() {}

    public Correspond(int idVente, int idProduit, int quantite, double montant) {
        this.idVente = idVente;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.montant = montant;
    }

    public int getIdVente() { return idVente; }
    public int getIdProduit() { return idProduit; }
    public int getQuantite() { return quantite; }
    public double getMontant() { return montant; }

    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setMontant(double montant) { this.montant = montant; }
}
