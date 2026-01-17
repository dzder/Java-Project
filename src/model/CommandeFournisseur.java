package model;

import java.util.Date;

public class CommandeFournisseur {
    private int idCommande;
    private Date dateCommande;
    private String statut; // en_attente, recu, annule, finalise
    private int idFour;

    public CommandeFournisseur() {}

    public CommandeFournisseur(int idCommande, Date dateCommande, String statut, int idFour) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.idFour = idFour;
    }

    public int getIdCommande() { return idCommande; }
    public Date getDateCommande() { return dateCommande; }
    public String getStatut() { return statut; }
    public int getIdFour() { return idFour; }

    public void setStatut(String statut) { this.statut = statut; }
}
