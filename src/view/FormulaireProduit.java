package view;

import javax.swing.*;
import java.awt.*;
import model.Produit;
import dao.ProduitDAO;

public class FormulaireProduit extends JDialog {
    private JTextField txtNom = new JTextField(15);
    private JTextField txtPrix = new JTextField(15);
    private JTextField txtQuantite = new JTextField(15);
    private JTextField txtSeuil = new JTextField(15);
    private boolean valide = false;

    public FormulaireProduit(Frame parent) {
        super(parent, "Ajouter un Produit", true); // true pour rendre la fenêtre modale
        setLayout(new BorderLayout());
        setSize(350, 250);
        setLocationRelativeTo(parent);

        // Panneau de saisie
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Nom :")); panelForm.add(txtNom);
        panelForm.add(new JLabel("Prix :")); panelForm.add(txtPrix);
        panelForm.add(new JLabel("Quantité :")); panelForm.add(txtQuantite);
        panelForm.add(new JLabel("Seuil Minimal :")); panelForm.add(txtSeuil);

        // Panneau Boutons
        JButton btnEnregistrer = new JButton("Enregistrer");
        JButton btnAnnuler = new JButton("Annuler");
        
        JPanel panelBtns = new JPanel();
        panelBtns.add(btnEnregistrer);
        panelBtns.add(btnAnnuler);

        add(panelForm, BorderLayout.CENTER);
        add(panelBtns, BorderLayout.SOUTH);

        // Actions
        btnAnnuler.addActionListener(e -> dispose());
        
        btnEnregistrer.addActionListener(e -> {
            try {
                // 1. Récupération et validation simple
                String nom = txtNom.getText();
                double prix = Double.parseDouble(txtPrix.getText());
                int qte = Integer.parseInt(txtQuantite.getText());
                int seuil = Integer.parseInt(txtSeuil.getText());

                // 2. Création de l'objet et appel au DAO
                Produit p = new Produit(0, nom, prix, qte, seuil);
                new ProduitDAO().ajouter(p);

                valide = true;
                dispose(); // Fermer la fenêtre
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir des nombres valides pour le prix et les quantités.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isValide() { return valide; }
}