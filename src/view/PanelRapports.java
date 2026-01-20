package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.RapportDAO;

public class PanelRapports extends JPanel {
    private RapportDAO rDao = new RapportDAO();
    private JTable tableStocks, tableCA, tableFournisseurs;

    public PanelRapports() {
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabs = new JTabbedPane();

        // 1. Onglet État des Stocks (Alertes)
        tabs.addTab("🚨 Alertes Stocks", creerPanelStocks());

        // 2. Onglet Chiffre d'Affaires
        tabs.addTab("💰 Chiffre d'Affaires", creerPanelCA());

        // 3. Onglet Performance Fournisseurs
        tabs.addTab("🤝 Fournisseurs", creerPanelFournisseurs());

        add(tabs, BorderLayout.CENTER);
        
        // Bouton de rafraîchissement global
        JButton btnActualiser = new JButton("Actualiser les données");
        btnActualiser.addActionListener(e -> actualiserTout());
        add(btnActualiser, BorderLayout.SOUTH);
    }

    private JPanel creerPanelStocks() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"Produit", "Stock Actuel", "Seuil Minimal", "État"};
        tableStocks = new JTable(new DefaultTableModel(cols, 0));
        p.add(new JScrollPane(tableStocks), BorderLayout.CENTER);
        p.add(new JLabel(" Liste des produits en dessous ou proches du seuil critique"), BorderLayout.NORTH);
        return p;
    }

    private JPanel creerPanelCA() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"Période", "Nombre Ventes", "Total CA (€)"};
        tableCA = new JTable(new DefaultTableModel(cols, 0));
        p.add(new JScrollPane(tableCA), BorderLayout.CENTER);
        return p;
    }

    private JPanel creerPanelFournisseurs() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"Fournisseur", "Commandes Reçues", "Total Dépensé (€)"};
        tableFournisseurs = new JTable(new DefaultTableModel(cols, 0));
        p.add(new JScrollPane(tableFournisseurs), BorderLayout.CENTER);
        return p;
    }

    public void actualiserTout() {
        rDao.remplirTableauStocks((DefaultTableModel) tableStocks.getModel());
        rDao.remplirTableauCA((DefaultTableModel) tableCA.getModel());
        rDao.remplirTableauFournisseurs((DefaultTableModel) tableFournisseurs.getModel());
    }
}