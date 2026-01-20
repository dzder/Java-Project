package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import dao.CommandeDAO;
import model.CommandeFournisseur;

public class PanelCommande extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private CommandeDAO cDao = new CommandeDAO();

    public PanelCommande() {
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("📦 Réception Commandes Fournisseurs");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnRecu = new JButton("Valider Réception ✅");
        top.add(title, BorderLayout.WEST);
        top.add(btnRecu, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        // Dans le constructeur de PanelCommande
JButton btnNouvelle = new JButton("Nouvelle Commande ➕");
// Ajoutez btnNouvelle au panneau 'top' ou 'actions'
top.add(btnNouvelle, BorderLayout.CENTER); // ou ajustez selon votre layout

btnNouvelle.addActionListener(e -> {
    FormulaireCommande diag = new FormulaireCommande((Frame) SwingUtilities.getWindowAncestor(this));
    diag.setVisible(true);
    if (diag.isSucces()) {
        chargerDonnees(); // Rafraîchir la table après création
    }
});

        // --- Table ---
        String[] cols = {"ID Commande", "Date", "ID Fournisseur", "Statut"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Action ---
        btnRecu.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Object idObj = model.getValueAt(row, 0);
            if (idObj == null) {
                JOptionPane.showMessageDialog(this, "Erreur : ID de commande invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = (int) idObj;
            String statut = (String) model.getValueAt(row, 3);
            
            if (statut != null && statut.equalsIgnoreCase("en_attente")) {
                receptionner(id);
            } else {
                JOptionPane.showMessageDialog(this, "Cette commande a déjà été traitée.", "Commande traitée", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        chargerDonnees();
    }

    private void chargerDonnees() {
        model.setRowCount(0);
        List<CommandeFournisseur> list = cDao.lireToutes();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (CommandeFournisseur c : list) {
            String dateStr = c.getDateCommande() != null ? sdf.format(c.getDateCommande()) : "N/A";
            model.addRow(new Object[]{
                c.getIdCommande(), 
                dateStr, 
                c.getIdFour(), 
                c.getStatut()
            });
        }
    }

    private void receptionner(int id) {
        try {
            cDao.receptionner(id);
            JOptionPane.showMessageDialog(this, "Stock mis à jour !");
            chargerDonnees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}