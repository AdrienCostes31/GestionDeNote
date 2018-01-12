/*
 * Alerte.java                                   17 déc. 2017
 * IUT info2 2017-2018 groupe 1, pas de droits
 */
package application.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Fenêtres d'alerte pouvant être lancées dans l'application.
 * @author Mickaël Dalbin
 */
public class Alerte {
    /**
     * fonction permettant de lancer une boite de dialogue de type erreur 
     * @param msg le message à afficher 
     */
    public static void affichAlerte(String msg) {
        
        // type d'alerte
        Alert alert = new Alert(AlertType.ERROR);
        
        // taille de la fenêtre modifiable par l'utilisateur
        alert.setResizable(true);
        
        // Titre et message
        alert.setTitle("Erreur");
        alert.setContentText(msg);
        
        // Attente d'une action de l'utilisateur
        alert.showAndWait();
    }

    /**
     * Renvoie une fenêtre d'alerte de type confirmation
     * @param msg le message à afficher
     * @return l'alerte à déclencher
     */
    public static Alert affichConfirmation (String msg) {
        
        // Type d'alerte
        Alert alert = new Alert(AlertType.CONFIRMATION);
        
        // Taille modifiable par l'utilisateur
        alert.setResizable(true);
        
        // Titre et message
        alert.setTitle("Confirmation");
        alert.setContentText(msg);

        return alert;
    }
}
