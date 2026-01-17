package model;

import java.util.Date;

public class Vente {
    private int idVente;
    private double montantVente;
    private Date dateVente;
    private int idUtil;
    private int idClient;

    public Vente() {}

    public Vente(int idVente, double montantVente, Date dateVente, int idUtil, int idClient) {
        this.idVente = idVente;
        this.montantVente = montantVente;
        this.dateVente = dateVente;
        this.idUtil = idUtil;
        this.idClient = idClient;
    }

    public int getIdVente() { return idVente; }
    public double getMontantVente() { return montantVente; }
    public Date getDateVente() { return dateVente; }
    public int getIdUtil() { return idUtil; }
    public int getIdClient() { return idClient; }

    public void setMontantVente(double montantVente) { this.montantVente = montantVente; }
}
