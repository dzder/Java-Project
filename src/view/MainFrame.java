package view;

import javax.swing.*;
import java.awt.*;
import dao.*;
import model.Utilisateur;
import exception.AuthentificationException;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);
    
    // Utilisateur actuellement connecté
    private Utilisateur currentUser;

    public MainFrame() {
        setTitle("PharmaGestion Pro v1.0");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ajout des écrans (Cards)
        container.add(creerPanelLogin(), "LOGIN");
        
        add(container);
        cardLayout.show(container, "LOGIN");
    }

    private JPanel creerPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 245, 240)); // Vert clair pharma
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Se connecter");
        loginBtn.setBackground(new Color(40, 167, 69));
        loginBtn.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Utilisateur:"), gbc);
        gbc.gridx = 1; panel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Mot de passe:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            try {
                UtilisateurDAO uDao = new UtilisateurDAO();
                currentUser = uDao.authentifier(userField.getText(), new String(passField.getPassword()));
                
                // Si succès, on charge le menu principal
                container.add(new MenuPrincipalPanel(this, currentUser), "MENU");
                cardLayout.show(container, "MENU");
                
            } catch (AuthentificationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}