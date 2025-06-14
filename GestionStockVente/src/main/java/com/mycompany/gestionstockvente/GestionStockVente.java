/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gestionstockvente;
//import com.mycompany.gestionstockvente.ui.LoginUI;
import com.mycompany.gestionstockvente.form.Login;
import javax.swing.SwingUtilities;
//import com.mycompany.gestionstockvente.model.Utilisateur;
//import com.mycompany.gestionstockvente.service.ProduitService;
//import com.mycompany.gestionstockvente.ui.HomeUI;
//import com.mycompany.gestionstockvente.ui.ProduitUI;
/**
 *
 * @author Admin
 */
public class GestionStockVente {

    public static void main(String[] args) {
            // Lancer l'interface graphique dans le thread de l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
       
    }
}
