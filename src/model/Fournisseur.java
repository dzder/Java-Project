package model;

public class Fournisseur {
    private int idFour;
    private String nom;
    private String contact;

    public Fournisseur() {}

    public Fournisseur(int idFour, String nom, String contact) {
        this.idFour = idFour;
        this.nom = nom;
        this.contact = contact;
    }

    public int getIdFour() { return idFour; }
    public String getNom() { return nom; }
    public String getContact() { return contact; }

    public void setNom(String nom) { this.nom = nom; }
    public void setContact(String contact) { this.contact = contact; }
}
