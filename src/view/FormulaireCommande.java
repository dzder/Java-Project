package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import dao.CommandeDAO;
import model.Concerne;

public class FormulaireCommande extends JDialog {
    private JTextField txtIdFour = new JTextField(5);
    private JTextField txtIdProd = new JTextField(5);
    private JTextField txtQte = new JTextField(5);
    private JTextField txtPrixAchat = new JTextField(5);
    
    private DefaultTableModel modelArticles;
    private List<Concerne> articles = new ArrayList<>();
    private boolean succes = false;

    public FormulaireCommande(Frame parent) {
        super(parent, "Créer une Commande Fournisseur", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --- HAUT : Saisie Fournisseur et Produit ---
        JPanel pnlSaisie = new JPanel(new GridLayout(3, 1));
        
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(new JLabel("ID Fournisseur :")); p1.add(txtIdFour);
        
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(new JLabel("ID Produit :")); p2.add(txtIdProd);
        p2.add(new JLabel("Qté :")); p2.add(txtQte);
        p2.add(new JLabel("Prix Unit :")); p2.add(txtPrixAchat);
        
        JButton btnAjouter = new JButton("Ajouter l'article");
        pnlSaisie.add(p1);
        pnlSaisie.add(p2);
        pnlSaisie.add(btnAjouter);
        add(pnlSaisie, BorderLayout.NORTH);

        // --- CENTRE : Liste des articles ajoutés ---
        modelArticles = new DefaultTableModel(new String[]{"ID Produit", "Quantité", "Prix Unit"}, 0);
        add(new JScrollPane(new JTable(modelArticles)), BorderLayout.CENTER);

        // --- BAS : Validation ---
        JButton btnValider = new JButton("Enregistrer la Commande");
        add(btnValider, BorderLayout.SOUTH);

        // --- LOGIQUE ---
        btnAjouter.addActionListener(e -> {
            try {
                int idProd = Integer.parseInt(txtIdProd.getText());
                int qte = Integer.parseInt(txtQte.getText());
                double prix = Double.parseDouble(txtPrixAchat.getText());
                
                modelArticles.addRow(new Object[]{idProd, qte, prix});
                articles.add(new Concerne(0, idProd, qte, prix)); // idCommande sera mis plus tard
                
                txtIdProd.setText(""); txtQte.setText(""); txtPrixAchat.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Saisie invalide");
            }
        });

        btnValider.addActionListener(e -> {
            if (articles.isEmpty() || txtIdFour.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Panier ou fournisseur vide !");
                return;
            }
            try {
                int idFour = Integer.parseInt(txtIdFour.getText());
                new CommandeDAO().creerCommandeComplete(idFour, articles);
                succes = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });
    }

    public boolean isSucces() { return succes; }
}