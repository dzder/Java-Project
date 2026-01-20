package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.ProduitDAO;
import model.Produit;

public class PanelStock extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProduitDAO produitDAO = new ProduitDAO();

    public PanelStock() {
        setLayout(new BorderLayout());

        // 1. Titre et Barre d'outils
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel(" Gestion des Stocks & Inventaire", JLabel.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnActualiser = new JButton("Actualiser");
        JButton btnAjouter = new JButton("Nouveau Produit");
        JButton btnSupprimer = new JButton("Supprimer");
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnActualiser);
        btnPanel.add(btnAjouter);
        btnPanel.add(btnSupprimer);
        
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 2. Configuration du Tableau
        String[] colonnes = {"ID", "Nom", "Prix", "Quantité", "Seuil Minimal", "État"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Lecture seule
        };
        
        table = new JTable(tableModel);
        configurerApparenceTableau();
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Événements
        btnActualiser.addActionListener(e -> chargerDonnees());
        
        btnSupprimer.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce produit ?");
                if (confirm == JOptionPane.YES_OPTION) {
                    produitDAO.supprimer(id);
                    chargerDonnees();
                }
            }
        });

        // Charger les données au démarrage
        chargerDonnees();
        btnAjouter.addActionListener(e -> {
    // On ouvre une boîte de dialogue personnalisée
    FormulaireProduit dialog = new FormulaireProduit((Frame) SwingUtilities.getWindowAncestor(this));
    dialog.setVisible(true);
    
    // Si l'utilisateur a validé, on rafraîchit le tableau
    if (dialog.isValide()) {
        chargerDonnees();
    }
});
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0); // Effacer le tableau
        List<Produit> produits = produitDAO.lireTous();
        
        for (Produit p : produits) {
            String etat = (p.getQuantite() <= p.getSeuilMinimal()) ? "CRITIQUE" : "OK";
            tableModel.addRow(new Object[]{
                p.getIdProduit(),
                p.getNomProduit(),
                p.getPrix() + " €",
                p.getQuantite(),
                p.getSeuilMinimal(),
                etat
            });
        }
    }

    private void configurerApparenceTableau() {
        // Rendu personnalisé pour les alertes de couleur
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Récupérer la quantité et le seuil de la ligne actuelle
                int quantite = (int) table.getModel().getValueAt(row, 3);
                int seuil = (int) table.getModel().getValueAt(row, 4);

                if (quantite <= seuil) {
                    c.setBackground(new Color(255, 200, 200)); // Rouge clair pour alerte
                    c.setForeground(Color.RED);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                return c;
            }
        });
    }
}