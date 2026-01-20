package view;

import javax.swing.*;
import java.awt.*;
import model.Client;
import dao.ClientDAO;

public class FormulaireClient extends JDialog {
    private JTextField txtNom = new JTextField(15);
    private JTextField txtEmail = new JTextField(15);
    private JTextField txtTelephone = new JTextField(15);
    private boolean succes = false;

    public FormulaireClient(Frame parent, String emailParDefaut) {
        super(parent, "Nouveau Client", true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        txtEmail.setText(emailParDefaut); // Pré-remplir avec l'email saisi dans PanelVente

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(new JLabel("Nom :")); p.add(txtNom);
        p.add(new JLabel("Email :")); p.add(txtEmail);
        p.add(new JLabel("Tél :")); p.add(txtTelephone);

        JButton btnEnregistrer = new JButton("Enregistrer");
        btnEnregistrer.addActionListener(e -> {
            if(!txtNom.getText().isEmpty() && !txtEmail.getText().isEmpty()) {
                // Ici, on appelle le ClientDAO pour insérer dans la BD
                // clientDao.ajouter(new Client(0, txtNom.getText(), txtEmail.getText(), txtTelephone.getText()));
                succes = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nom et Email obligatoires !");
            }
        });

        add(p, BorderLayout.CENTER);
        add(btnEnregistrer, BorderLayout.SOUTH);
    }

    public boolean isSucces() { return succes; }
    public String getNomSaisi() { return txtNom.getText(); }
    
    public String getEmailSaisi() { return txtEmail.getText(); }
}