package model;

public class Client {
    private int idClient;
    private String nomPrenom;
    private String mail;

    public Client() {}

    public Client(int idClient, String nomPrenom, String mail) {
        this.idClient = idClient;
        this.nomPrenom = nomPrenom;
        this.mail = mail;
    }

    public int getIdClient() { return idClient; }
    public String getNomPrenom() { return nomPrenom; }
    public String getMail() { return mail; }

    public void setNomPrenom(String nomPrenom) { this.nomPrenom = nomPrenom; }
    public void setMail(String mail) { this.mail = mail; }
}
