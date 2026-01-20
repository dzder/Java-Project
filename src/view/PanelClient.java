package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ClientDAO;
import dao.RapportDAO;
import model.Client;

public class PanelClient extends JPanel {
    private JTable tableClients, tableHistorique;
    private DefaultTableModel modelClients, modelHistorique;
    private JTextField txtRecherche = new JTextField(20);
    private ClientDAO cDao = new ClientDAO();
    private RapportDAO rDao = new RapportDAO();

    public PanelClient() {
        setLayout(new GridLayout(2, 1, 10, 10)); // Deux zones égales

        // --- ZONE SUPÉRIEURE : Liste des Clients ---
        JPanel pnlHaut = new JPanel(new BorderLayout());
        pnlHaut.setBorder(BorderFactory.createTitledBorder("Gestion des Clients"));

        JPanel pnlOutils = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlOutils.add(new JLabel("Chercher par Nom/Email :"));
        pnlOutils.add(txtRecherche);
        JButton btnChercher = new JButton("🔍");
        pnlOutils.add(btnChercher);
        JButton btnActualiser = new JButton("🔄 Actualiser");
        pnlOutils.add(btnActualiser);

        modelClients = new DefaultTableModel(new String[]{"ID", "Nom", "Email"}, 0);
        tableClients = new JTable(modelClients);
        pnlHaut.add(pnlOutils, BorderLayout.NORTH);
        pnlHaut.add(new JScrollPane(tableClients), BorderLayout.CENTER);

        // --- ZONE INFÉRIEURE : Historique des Achats ---
        JPanel pnlBas = new JPanel(new BorderLayout());
        pnlBas.setBorder(BorderFactory.createTitledBorder("Historique des achats du client sélectionné"));

        modelHistorique = new DefaultTableModel(new String[]{"Date", "Produit", "Quantité", "Total"}, 0);
        tableHistorique = new JTable(modelHistorique);
        pnlBas.add(new JScrollPane(tableHistorique), BorderLayout.CENTER);

        add(pnlHaut);
        add(pnlBas);

        // --- LOGIQUE ---

        // 1. Charger les clients au démarrage
        actualiserClients("");

        // 2. Recherche dynamique
        btnChercher.addActionListener(e -> actualiserClients(txtRecherche.getText()));
        // Action Actualiser (Reset le filtre et recharge tout)
        btnActualiser.addActionListener(e -> {
            txtRecherche.setText(""); // Vide le champ de recherche
            actualiserClients("");    // Recharge tous les clients
            modelHistorique.setRowCount(0); // Optionnel : vide aussi l'historique affiché
        });

        // 3. Action lors de la sélection d'un client : Charger son historique
        tableClients.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableClients.getSelectedRow();
                if (row != -1) {
                    String email = (String) modelClients.getValueAt(row, 2);
                    chargerHistoriqueClient(email);
                }
            }
        });
    }

    private void actualiserClients(String filtre) {
        modelClients.setRowCount(0);
        List<Client> liste = cDao.rechercherClients(filtre);
        for (Client c : liste) {
            modelClients.addRow(new Object[]{c.getIdClient(), c.getNomPrenom(), c.getMail()});
        }
    }

    private void chargerHistoriqueClient(String email) {
        // On réutilise le RapportDAO pour remplir le tableau du bas
        rDao.remplirHistoriqueAchats(modelHistorique, email);
    }
}