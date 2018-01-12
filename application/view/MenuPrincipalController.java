package application.view;

import java.io.IOException;
import java.util.Optional;

import com.itextpdf.text.DocumentException;

import application.model.GestionDeNotes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Fenêtre de menu principal de l'application. Permet de lancer les
 * différentes fonctionnalités.
 * @author Adrien Costes et Mickaël Dalbin
 */
public class MenuPrincipalController {
    
    /** Fenêtre */
    private Stage fenetre = new Stage();
    
    /** Bouton d'ajout d'une promotion */
    @FXML
    private Button btn_ajoutEtudiant;
    
    /** Bouton de suppression d'une promotion */
    @FXML
    private Button btn_supprEtudiant;
    
    /** Bouton de génération de relevés de notes */
    @FXML
    private Button btn_genererReleve;
    
    /** Bouton de génération d'un tableau synthétique pour une promotion */
    @FXML
    private Button btn_genererTabl;
    
    /** Bouton d'ajout d'un contrôle */
    @FXML
    private Button btn_ajouterNote;
    
    /** Bouton de modification des notes */
    @FXML
    private Button btn_modifNote;
    
    /** Bouton d'affichage de la moyenne d'un contrôle */
    @FXML
    private Button btn_affMoyenneCtrl;    
    
    /** Bouton de suppression d'un contrôle */
    @FXML
    private Button btnSuppr;
    
    /** Bouton de changement de mot de passe */
    @FXML
    private Button btnChgMdp;
    
    /** Bouton de fermeture de l'application */
    @FXML
    private Button btnQuitter;

    /** Instance du menu principal */
    private static MenuPrincipalController instance;

    /**
     * @return l'instance du menu principal
     */
    public static MenuPrincipalController getInstance() {
        if(instance==null) {
            instance = new MenuPrincipalController();
        }
        return instance;
    }

    /**
     * Crée une instance du menu principal
     */
    public MenuPrincipalController() {
        super();
    }

    /**
     * Lance la fenêtre du menu principal
     */
    public void showMenu() {

        try {
            
            // Chargement du fichier FXML
            Parent pageP = FXMLLoader.load(getClass().getResource("MenuPrincipal.fxml"));
            
            // Création de la scène et affectation à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // Définition du titre de fenêtre
            fenetre.setTitle("Gestion de notes");
            
            // Affichage de la fenêtre
            fenetre.show();

        } catch (IOException e) {
            
            Alerte.affichAlerte("Impossible de charger la fenêtre.");
        }
    }

    /**
     * Ouvre la fenêtre de changement de mot de passe
     * @param event l'événement de type clic
     */
    public void changerMdp(ActionEvent event) {
        ModifierMdpController.getInstance().showModifierMdp();
    }

    /**
     * affichage de la fenêtre d'ajout de promotion
     * @param event l'événement de type clic
     */
    public void ajoutPromotion(ActionEvent event) {
        //lance la fenetre
        AjouterPromotionController.getInstance().showAjouterPromotion();
    }

    /**
     * Ouvre la fenêtre d'ajout de contrôle
     * @param event l'événement de type clic
     */
    public void ajoutNote(ActionEvent event) {
        AjouterNoteController.getInstance().showAjouterNote();
    }
    
    /**
     * Ouvre la fenêtre de sélection d'un contrôle pour afficher
     * la moyenne de la promotion de ce dernier
     * @param event l'événement de type clic
     */
    public void afficherMoyenne(ActionEvent event) {
        AfficherMoyenneController.getInstance().showAfficherMoyenne();
    }
    
    /**
     * Ouvre la fenêtre de suppression d'un contrôle
     * @param event l'événement de type clic
     */
    public void supprCtrl(ActionEvent event) {
        
        SupprimerControleController.getInstance().showSupprimerControle();
    }

    /**
     * Génère le relevé de tous les étudiants de la promotion
     * @param event l'événement de type clic
     */
    public void genererReleves(ActionEvent event) {

        try {
            GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
            // On demande à l'utilisateur s'il veut vraiment générer les relevés
            Alert confirm = Alerte.affichConfirmation("Vous allez générer les relevés de notes pour la promotion "
                                                    + gdn.getListeSemestres().get(0).getPromo().getNom() + ".\n"
                                                    + "S'il existe des relevés pour ces étudiants, ils seront remplacés.\n"
                                                    + "\n Souhaitez-vous vraiment continuer ?");
            // On attend la réponse de l'utilisateur
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.get() == ButtonType.OK){
                // On génère les relevés
                gdn.getListeSemestres().get(0).getPromo().genererTousReleves();
            } 
        } catch (ClassNotFoundException e) {
            Alerte.affichAlerte("Le chargement de l'application a échoué.");
        } catch (DocumentException e) {
            Alerte.affichAlerte("Un problème est survenu lors de la création des relevés.");
        } catch (NullPointerException e) {
            Alerte.affichAlerte("Aucune promotion n'est enregistrée.");
        }
    }
    
    /**
     * Quitte l'application
     * @param envent l'événement de type clic
     */
    public void quitter(ActionEvent envent) {
        Stage stage = (Stage) btnQuitter.getScene().getWindow();
        stage.close();
    }
}
