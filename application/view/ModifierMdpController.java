package application.view;

import java.io.IOException;
import java.util.Optional;

import application.model.GestionDeNotes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Fenêtre de modification du mot de passe utilisateur, lancée depuis
 * le menu principal de l'application.
 * @author Mickaël Dalbin
 */
public class ModifierMdpController {

    /** Fenêtre */
    Stage fenetre = new Stage();

    /** Champ de texte contenant le nouveau mot de passe */
    @FXML
    private PasswordField newMdp;

    /** Champ de texte contenant la confirmation du nouveau mdp */
    @FXML
    private PasswordField confirm;

    /** Bouton de validation */
    @FXML
    private Button btn_valid;

    /** Instance de la fenêtre */
    private static ModifierMdpController instance;
    
    /**
     * @return l'instance de la fenêtre
     */
    public static ModifierMdpController getInstance() {
        if(instance==null) {
            instance = new ModifierMdpController();
        }
        return instance;
    }

    /**
     * Crée une instance de la fenêtre
     */
    public ModifierMdpController() {
        super();
    }
    
    /**
     * Lance la fenêtre d'ajout de promotion
     */
    public void showModifierMdp() {
        initStage();
        fenetre.show();
    }

    /**
     * Initialise la fenêtre à afficher
     */
    private void initStage() {  

        try {
            
            Parent pageP;
            
            // Chargement du fichier FXML
            pageP = FXMLLoader.load(getClass().getResource("ModifierMdp.fxml"));
            
            // Création de la scène et affectation à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // Définition du titre de la fenêtre
            fenetre.setTitle("Gestion de notes - Modifier le mot de passe");

        } catch (IOException e) {

            Alerte.affichAlerte("Impossible de charger la fenêtre.");
        }
    }
    
    /**
     * Ouvre une fenêtre de confirmation et ajoute ou non la promotion
     * en fonction du choix de l'utilisateur
     * @param event l'événement de type clic
     */
    @FXML
    public void btnValider(ActionEvent event) {
        
        // On contrôle que les deux champs soient non vides
        if (newMdp.getText().isEmpty() || confirm.getText().isEmpty()) {
            Alerte.affichAlerte("Un des deux champs est vide.");
        } else {
            try {
                // On charge les données de l'application
                GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
                
                // On vérifie que le nouveau mot de passe et sa confirmation sont identiques
                if (newMdp.getText().equals(confirm.getText())) {
                    
                    // On ouvre une fenêtre de confirmation
                    Alert confirmation = Alerte.affichConfirmation("Souhaitez-vous vraiment changer votre mot de passe ?");
                    
                    // On attend la réponse de l'utilisateur
                    Optional<ButtonType> result = confirmation.showAndWait();
                    
                    if (result.get() == ButtonType.OK){
                        
                        // On remplace l'ancien mot de passe par le nouveau
                        gdn.setMdp(newMdp.getText());
                        
                        // On sauvegarde l'application
                        gdn.sauvegarderGestionNotes();
                        
                        // fermeture de la fenêtre
                        Stage stage = (Stage) btn_valid.getScene().getWindow();
                        stage.close();
                    }
                    
                } else {
                    
                    // On remet les champs à zéro
                    newMdp.setText("");
                    confirm.setText("");
                    
                    // On affiche un message d'erreur
                    Alerte.affichAlerte("Vous devez saisir deux fois le même mot de passe.");
                }
                
            } catch (ClassNotFoundException e) {
                Alerte.affichAlerte("Le chargement de l'application a échoué.");
            }
        }               
    } 
}
