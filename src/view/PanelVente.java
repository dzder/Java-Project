package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.ClientDAO;
import dao.ProduitDAO;
import dao.VenteDAO;
import model.*;
import exception.*;

public class PanelVente extends JPanel {
    // Composants de saisie
    private JTextField txtEmailClient = new JTextField(15);
    private JTextField txtIdProduit = new JTextField(5);
    private JTextField txtQuantite = new JTextField(5);
    
    // Composants d'affichage
    private JTable tablePanier;
    private DefaultTableModel modelPanier;
    private JLabel lblTotal = new JLabel("Total : 0.00 €");
    private double totalVente = 0;

    // Données et DAOs
    private List<Correspond> listeArticles = new ArrayList<>();
    private ProduitDAO pDao = new ProduitDAO();
    private VenteDAO vDao = new VenteDAO();
    private ClientDAO cDao = new ClientDAO();

    public PanelVente() {
        setLayout(new BorderLayout(10, 10));

        // --- PARTIE HAUTE : Saisie (Correction des doublons) ---
        JPanel panelSaisie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Section Client
        panelSaisie.add(new JLabel("Email Client :"));
        panelSaisie.add(txtEmailClient);
        
        JButton btnNouveauClient = new JButton("+");
        btnNouveauClient.setToolTipText("Créer un nouveau client");
        panelSaisie.add(btnNouveauClient); 
        
        panelSaisie.add(new JLabel(" | ID Produit :"));
        panelSaisie.add(txtIdProduit);
        
        panelSaisie.add(new JLabel("Qté :"));
        panelSaisie.add(txtQuantite);
        
        JButton btnAjouter = new JButton("Ajouter au Panier");
        panelSaisie.add(btnAjouter);
        
        add(panelSaisie, BorderLayout.NORTH);

        // --- PARTIE CENTRALE : Panier ---
        String[] colonnes = {"ID", "Désignation", "Prix Unitaire", "Qté", "Sous-total"};
        modelPanier = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablePanier = new JTable(modelPanier);
        add(new JScrollPane(tablePanier), BorderLayout.CENTER);

        // --- PARTIE BASSE : Total et Validation ---
        JPanel panelBas = new JPanel(new BorderLayout());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotal.setForeground(new Color(0, 100, 0));
        
        JButton btnValider = new JButton("💰 Valider et Encaisser");
        btnValider.setPreferredSize(new Dimension(200, 50));
        btnValider.setBackground(new Color(40, 167, 69));
        btnValider.setForeground(Color.WHITE);

        panelBas.add(lblTotal, BorderLayout.WEST);
        panelBas.add(btnValider, BorderLayout.EAST);
        add(panelBas, BorderLayout.SOUTH);

        // --- GESTION DES ÉVÉNEMENTS ---

        btnAjouter.addActionListener(e -> ajouterAuPanier());

        btnValider.addActionListener(e -> validerVente());

        btnNouveauClient.addActionListener(e -> {
            // Récupère la fenêtre parente (MainFrame) pour le dialogue
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            FormulaireClient diag = new FormulaireClient(parent, txtEmailClient.getText());
            diag.setVisible(true);
            
            if (diag.isSucces()) {
                txtEmailClient.setText(diag.getEmailSaisi());
                cDao.ajouterClient(new Client(0, diag.getNomSaisi(), diag.getEmailSaisi()));
                JOptionPane.showMessageDialog(this, "Client créé avec succès !");
            }
        });
    }

    private void ajouterAuPanier() {
        try {
            // Vérification des saisies numériques
            int id = Integer.parseInt(txtIdProduit.getText());
            int qte = Integer.parseInt(txtQuantite.getText());

            // 1. Recherche du produit (Variable déclarée à l'extérieur du bloc try)
            Produit p = null; 
            try {
                p = pDao.trouverParId(id);
            } catch (EntityNotFoundException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur technique : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Traitement si le produit existe
            if (p != null) {
                // Vérification locale du stock avant ajout au panier
                if (qte > p.getQuantite()) {
                    JOptionPane.showMessageDialog(this, 
                        "Stock insuffisant !\nDisponible : " + p.getQuantite(), 
                        "Alerte Stock", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double sousTotal = p.getPrix() * qte;
                
                // Mise à jour de la vue (Tableau)
                modelPanier.addRow(new Object[]{id, p.getNomProduit(), p.getPrix(), qte, sousTotal});
                
                // Mise à jour du modèle (Liste pour le DAO)
                listeArticles.add(new Correspond(0, id, qte, sousTotal));
                
                // Mise à jour du total
                totalVente += sousTotal;
                lblTotal.setText(String.format("Total : %.2f €", totalVente));
                
                // Nettoyage des champs produit pour la saisie suivante
                txtIdProduit.setText("");
                txtQuantite.setText("");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir des nombres valides (ID et Quantité).");
        }
    }

    private void validerVente() {
        if (listeArticles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le panier est vide !");
            return;
        }

        String email = txtEmailClient.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir l'email du client.");
            return;
        }

        // Récupération de l'ID client
        int idClient = cDao.trouverIdParEmail(email);
        if (idClient == -1) {
            JOptionPane.showMessageDialog(this, "Client introuvable : " + email + 
                "\nCliquez sur '+' pour le créer.");
            return;
        }

        try {
            // Création de l'objet vente (idUtil est mis à 1 par défaut ici)
            Vente v = new Vente(0, totalVente, new Date(), 1, idClient);
            
            // Enregistrement via le DAO (Gestion de la transaction SQL)
            vDao.enregistrerVente(v, listeArticles);

            JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès !");
            reinitialiserVente();

        } catch (StockInsuffisantException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur Stock", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la validation : " + ex.getMessage());
        }
    }

    private void reinitialiserVente() {
        modelPanier.setRowCount(0);
        listeArticles.clear();
        totalVente = 0;
        lblTotal.setText("Total : 0.00 €");
        txtIdProduit.setText("");
        txtQuantite.setText("");
        txtEmailClient.setText("");
    }
}