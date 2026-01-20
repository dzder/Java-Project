package view;

import javax.swing.*;
import java.awt.*;
import model.Utilisateur;

public class MenuPrincipalPanel extends JPanel {
    public MenuPrincipalPanel(MainFrame parent, Utilisateur user) {
        setLayout(new BorderLayout());

        // Barre d'info utilisateur
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.add(new JLabel("Connecté : " + user.getUsername() + " (" + user.getRole() + ") "));
        add(topBar, BorderLayout.NORTH);

        // Système d'onglets (Tabs)
        JTabbedPane tabs = new JTabbedPane();

        // 1. Onglet Ventes
        tabs.addTab("Ventes", new PanelVente());

        // 2. Onglet Stock (Ajouter, Modifier, Supprimer)
        tabs.addTab("Gestion Stock", new PanelStock());

        // 3. Onglet Commandes Fournisseurs
        tabs.addTab("Commandes", new PanelCommande());
        // 4. Onglet Clients
        tabs.addTab("Clients", new PanelClient());

        // 4. Onglet Rapports (Seulement si Admin)
        if (user.isAdmin()) {
            tabs.addTab("Rapports & Analyses", new PanelRapports());
        }

        add(tabs, BorderLayout.CENTER);
    }
}