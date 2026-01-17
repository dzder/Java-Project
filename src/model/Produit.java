package model;

public class Produit {
    private int idProduit;
    private String nomProduit;
    private double prix;
    private int quantite;
    private int seuilMinimal;

    public Produit() {}

    public Produit(int idProduit, String nomProduit, double prix, int quantite, int seuilMinimal) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.quantite = quantite;
        this.seuilMinimal = seuilMinimal;
    }

    public int getIdProduit() { return idProduit; }
    public String getNomProduit() { return nomProduit; }
    public double getPrix() { return prix; }
    public int getQuantite() { return quantite; }
    public int getSeuilMinimal() { return seuilMinimal; }

    public void setQuantite(int quantite) { this.quantite = quantite; }

    public boolean stockCritique() {
        return quantite <= seuilMinimal;
    }
}
