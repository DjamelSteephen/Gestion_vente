/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.gestionstockvente.form;

//import static cn.hutool.core.util.DesensitizedUtil.password;
//import static cn.hutool.core.util.URLUtil.url;
//import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.ui.CommandeUI;
import com.mycompany.gestionstockvente.ui.FactureUI;
//import com.mycompany.gestionstockvente.ui.HomeUI;
import com.mycompany.gestionstockvente.ui.ProduitUI;
import com.mycompany.gestionstockvente.ui.ReportingUI;
//import com.mycompany.gestionstockvente.ui.StockUI;
import com.mycompany.gestionstockvente.ui.VenteUI;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
//import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
//import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
//import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Admin
 */
public class Home extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Home.class.getName());
    
    private static final int SEUIL_REAPPRO = 10;
    private final ProduitService produitService;
    /**
     * Creates new form Home2
     * @param utilisateur
     */
    public Home(Utilisateur utilisateur) throws SQLException {
        initComponents();
        
        // Définir la taille souhaitée de la fenêtre (exemple : 800x500 pixels)
    //this.setSize(9z00, 520);

    // Centrer la fenêtre par rapport à l'écran
    this.setLocationRelativeTo(null);
        
        // Initialisation du service Produit
        produitService = new ProduitService();

        // Affichage de l'alerte stock dès l'ouverture de Home
        //afficherAlerteStock();
        
        // Ajouter un listener pour afficher l'alerte stock après ouverture de la fenêtre
this.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowOpened(java.awt.event.WindowEvent evt) {
        // Afficher le message d'alerte dès l'ouverture de la fenêtre Home
        JOptionPane.showMessageDialog(Home.this, "Bienvenue dans le tableau de bord!");
        afficherAlerteStock();
    }
});

        
    commande.addActionListener((ActionEvent e) -> {
        try {
            JOptionPane.showMessageDialog(Home.this, "Génération des Commandes...");
            new CommandeUI().setVisible(true);
        }catch (SQLException ex) {
            System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }   catch (Exception ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    
    produit.addActionListener(e -> {
        JOptionPane.showMessageDialog(Home.this, "Génération des Produits...");
            try { 
                new ProduitUI().setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

    // Action : ouvrir Ventes
    vente.addActionListener(e -> {
        JOptionPane.showMessageDialog(this, "Ouverture du module Ventes...");
        try {
            //JOptionPane.showMessageDialog(Home.this, "Génération des Commandes...");
            Connection conn = DatabaseConnection.getConnection();
            new VenteUI(conn).setVisible(true);
        }   catch (Exception ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
    });

    // Action : ouvrir Factures
    facture.addActionListener(e -> {
        
        try {
            JOptionPane.showMessageDialog(Home.this, "Génération des Factures...");
            Connection conn = DatabaseConnection.getConnection();
            new FactureUI(conn).setVisible(true);
        }   catch (Exception ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        //JOptionPane.showMessageDialog(this, "Ouverture du module Factures...");
    });
    
    
    report.addActionListener(e -> {
        
        try {
            JOptionPane.showMessageDialog(Home.this, "Génération des rapports...");
            //Connection conn = DatabaseConnection.getConnection();
            new ReportingUI().setVisible(true);
        }   catch (Exception ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        //JOptionPane.showMessageDialog(this, "Ouverture du module Factures...");
    });

    // Action : déconnexion
    deconnexion.addActionListener(e -> {
        dispose();
        new Login().setVisible(true);
    });
    }
    
    private void afficherAlerteStock() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Produit p : produitService.listerProduits()) {
                if (p.getQuantiteEnStock() <= SEUIL_REAPPRO) {
                    sb.append(String.format("– %s (stock %d ≤ seuil %d)%n",
                            p.getNom(), p.getQuantiteEnStock(), SEUIL_REAPPRO));
                }
            }
            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Alerte réapprovisionnement :\n" + sb,
                        "Attention", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la vérification du stock : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        produit = new javax.swing.JButton();
        vente = new javax.swing.JButton();
        facture = new javax.swing.JButton();
        deconnexion = new javax.swing.JButton();
        commande = new javax.swing.JButton();
        report = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 0, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 30)); // NOI18N
        jLabel2.setText("DASHBORD");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        produit.setBackground(new java.awt.Color(255, 204, 204));
        produit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        produit.setForeground(new java.awt.Color(0, 0, 255));
        produit.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\shopping_bag_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        produit.setText("Produit");
        produit.setBorder(null);
        produit.setOpaque(true);
        produit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produitActionPerformed(evt);
            }
        });

        vente.setBackground(new java.awt.Color(255, 204, 204));
        vente.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        vente.setForeground(new java.awt.Color(0, 0, 255));
        vente.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\shopping_cart_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        vente.setText("Vente");
        vente.setBorder(null);
        vente.setOpaque(true);
        vente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                venteActionPerformed(evt);
            }
        });

        facture.setBackground(new java.awt.Color(255, 204, 204));
        facture.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        facture.setForeground(new java.awt.Color(0, 0, 255));
        facture.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\whatshot_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        facture.setText("Facture");
        facture.setBorder(null);
        facture.setOpaque(true);
        facture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                factureActionPerformed(evt);
            }
        });

        deconnexion.setBackground(new java.awt.Color(255, 204, 204));
        deconnexion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        deconnexion.setForeground(new java.awt.Color(255, 0, 0));
        deconnexion.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\power_settings_circle_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        deconnexion.setText("Déconnexion");
        deconnexion.setBorder(null);
        deconnexion.setOpaque(true);
        deconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deconnexionActionPerformed(evt);
            }
        });

        commande.setBackground(new java.awt.Color(255, 204, 204));
        commande.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        commande.setForeground(new java.awt.Color(0, 0, 255));
        commande.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\receipt_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        commande.setText("Commande");
        commande.setBorder(null);
        commande.setOpaque(true);

        report.setBackground(new java.awt.Color(255, 204, 204));
        report.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        report.setForeground(new java.awt.Color(0, 0, 255));
        report.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\sell_30dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        report.setText("Report");
        report.setBorder(null);
        report.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deconnexion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(vente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(produit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(commande, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(facture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(report, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(produit, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commande, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vente, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(facture, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(report, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deconnexion, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 120)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Hel");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, 150));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 120)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 255));
        jLabel3.setText("lo!");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, 130));

        jLabel4.setBackground(new java.awt.Color(204, 204, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Good to see you yere");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 204));
        jLabel5.setText("LET'S START");
        jLabel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 255)));
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, -1, -1));
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setText("Service");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 380, -1, -1));
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel9.setText("Database");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 380, -1, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\icons8-base-de-données-48.png")); // NOI18N
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 50, 50));

        jLabel11.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\icons8-service-48.png")); // NOI18N
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 320, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel12.setText("Java");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 380, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon("C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\com\\mycompany\\gestionstockvente\\icon\\icons8-logo-java-coffee-cup-48.png")); // NOI18N
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 320, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deconnexionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deconnexionActionPerformed

    private void venteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_venteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_venteActionPerformed

    private void factureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_factureActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_factureActionPerformed

    private void produitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_produitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(() -> {
            //afficherAlerteStock();
            // Simuler un utilisateur connecté
            Utilisateur user = new Utilisateur(2, "Zongo", "steph", "root");
            try {
                new Home(user).setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(Home.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
        //java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton commande;
    private javax.swing.JButton deconnexion;
    private javax.swing.JButton facture;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton produit;
    private javax.swing.JButton report;
    private javax.swing.JButton vente;
    // End of variables declaration//GEN-END:variables
}
