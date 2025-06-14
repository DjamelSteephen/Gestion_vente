/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.ui;

import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.form.Home;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Stock;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.service.CommandeService;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class CommandeUI extends JFrame {
    private static final int SEUIL_REAPPRO = 10;

    private final JTextField nomClientField = new JTextField(15);
    private final JComboBox<Produit> produitCB = new JComboBox<>();
    private final JTextField quantiteField = new JTextField(5);

    private final JButton btnAjouter  = new JButton("Ajouter");
    private final JButton btnValider  = new JButton("Valider");
    private final JButton btnSupprimer= new JButton("Supprimer ligne");
    private final JButton btnRetour   = new JButton("Retour");

    private final DefaultTableModel model;
    private final JTable table;
    private final List<ContientCommandeProduit> panier = new ArrayList<>();

    private final ProduitService produitService;
    private final CommandeService commandeService;
    private final StockDAO stockDAO;

    public CommandeUI() throws SQLException {
        super("Gestion des Commandes");

        Connection conn = DatabaseConnection.getConnection();
        produitService  = new ProduitService();
        commandeService = new CommandeService(conn);
        stockDAO        = new StockDAO(conn);

        String[] cols = {"Produit", "Qté", "PU", "Sous-total", "Alerte"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        table = new JTable(model);
        // Colonne Alerte en rouge léger
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,
                    boolean sel,boolean foc,int row,int col){
                Component c = super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                if(col==4 && "⚠️".equals(v)) c.setBackground(new Color(255,230,230));
                else c.setBackground(Color.WHITE);
                return c;
            }
        });

        initLayout();
        loadProduits();
        attachListeners();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(900,520);
        setLocationRelativeTo(null);
        setVisible(true);   
    }

    private void initLayout(){
        setLayout(new BorderLayout(5,5));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,40,10));
        top.add(new JLabel("Client:"));   
        top.add(nomClientField);
        top.add(new JLabel("Produit:"));  
        top.add(produitCB);
        top.add(new JLabel("Quantité Voulue:"));
        top.add(quantiteField);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
        bot.add(btnAjouter);
        bot.add(btnSupprimer);
        bot.add(btnValider);
        bot.add(btnRetour);
        add(bot, BorderLayout.SOUTH);
    }

    private void loadProduits(){
        try {
            produitCB.removeAllItems();
            for(Produit p: produitService.listerProduits())
                produitCB.addItem(p);
        } catch(SQLException e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private void attachListeners(){
        btnAjouter.addActionListener(e->{
            Produit p = (Produit)produitCB.getSelectedItem();
            if(p==null) return;
            int q;
            try{ q=Integer.parseInt(quantiteField.getText()); }
            catch(Exception ex){ JOptionPane.showMessageDialog(this,"Quantité invalide"); return; }
            if(q<=0){ JOptionPane.showMessageDialog(this,"Quantité > 0"); return; }

            if(q>p.getQuantiteEnStock()){
                JOptionPane.showMessageDialog(this,"Stock insuffisant"); return;
            }
            boolean alert = p.getQuantiteEnStock() <= SEUIL_REAPPRO;
            double pu = p.getPrixUnitaire(), st = pu*q;
            model.addRow(new Object[]{
                p.getNom(), q,
                String.format("%.2f", pu),
                String.format("%.2f", st),
                alert ? "⚠️" : ""
            });
            panier.add(new ContientCommandeProduit(p,q));
            quantiteField.setText("");
        });

        btnSupprimer.addActionListener(e->{
            int r = table.getSelectedRow();
            if(r>=0){ model.removeRow(r); panier.remove(r); }
        });

        btnValider.addActionListener(e->{
            for(int i=0;i<model.getRowCount();i++){
                if("⚠️".equals(model.getValueAt(i,4))){
                    JOptionPane.showMessageDialog(this,
                        "Impossible : un produit est en alerte.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if(panier.isEmpty()||nomClientField.getText().isBlank()){
                JOptionPane.showMessageDialog(this,
                    "Client ou panier manquant.",
                    "Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }
            commandeService.creerCommande(nomClientField.getText(), panier);
            JOptionPane.showMessageDialog(this,
                "✅ Commande validée.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            panier.clear();
        });

        btnRetour.addActionListener(e->{
            dispose();
            SwingUtilities.invokeLater(()->{
                try {
                    new Home(new Utilisateur(2,"Zongo","steph","root")).setVisible(true);
                } catch (SQLException ex) {
                    System.getLogger(CommandeUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            });
        });
    }

//    }

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(()->{
            try{ new CommandeUI(); }
            catch(SQLException e){ JOptionPane.showMessageDialog(null,e.getMessage()); }
        });
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           