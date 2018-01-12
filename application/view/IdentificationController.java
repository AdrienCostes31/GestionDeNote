package application.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.model.GestionDeNotes;
import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

/**
 * Fenêtre d'authentification de l'utilisateur de l'application.
 * Elle permet d'accéder au menu principal de l'application.
 * @author Adrien Costes et Mickaël Dalbin
 */
public class IdentificationController {

    /** Champ de texte contenant l'identifiant de l'utilisateur */
    @FXML
    private TextField identifiant;

    /** Champ de texte contenant le mot de passe de l'utilisateur */
    @FXML
    private PasswordField password;

    /** Bouton de connexion */
    @FXML
    private Button btn_valider;

    /**
     * Ouvre la page de menu principal si la connexion est réussie
     * @param event
     */
    @FXML
    public void affichMenuPrincipal(ActionEvent event) {
        
        try {
            // Chargement des données de l'application
            GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
            
            if (identifiant.getText().equals(gdn.getIdentifiant()) 
                && password.getText().equals(gdn.getMdp())) { //si l'id et le mdp sont corrects,
                //lance la fenetre
    
                //réinitialise les champs
                identifiant.setText("");
                password.setText("");
    
                //lance la fenetre du menu principal
                MenuPrincipalController.getInstance().showMenu();
                
                // fermeture de la fenêtre d'identification
                Stage stage = (Stage) btn_valider.getScene().getWindow();
                stage.close();
    
            } else {
                
                // Alerte si l'id et le mdp sont incorrects
                if (!identifiant.getText().equals(gdn.getIdentifiant())
                    && !password.getText().equals(gdn.getMdp())) { 
                    
                    // réinitialisation des deux champs
                    identifiant.setText("");
                    password.setText("");
                    
                    Alerte.affichAlerte("Mot de passe et identifiant incorrects.");
                
                // Alerte si le mot de passe est incorrect
                } else if (!password.getText().equals(gdn.getMdp())) {
    
                    password.setText("");
                    Alerte.affichAlerte("Mot de passe incorrect.");
                
                // Alerte si l'id est incorrect
                } else {

                    //réinitialise le champs invalide
                    identifiant.setText(""); 
    
                    Alerte.affichAlerte("Identifiant incorrect.");
                }
            }
        } catch (ClassNotFoundException e) {
            Alerte.affichAlerte("Impossible de charger les données.");
        }
    }
}
