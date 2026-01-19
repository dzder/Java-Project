package model;

public class Utilisateur {
    private int idUtil;
    private String username;
    private String mdp;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int idUtil, String username, String mdp, String role) {
        this.idUtil = idUtil;
        this.username = username;
        this.mdp = mdp;
        this.role = role;
    }

    public int getIdUtil() { return idUtil; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getMdp() { return mdp; } // Ajouté

    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() { return "admin".equals(role); }
}
