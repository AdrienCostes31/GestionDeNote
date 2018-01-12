package application.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.model.*;
import application.model.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.stage.Stage;

/**
 * Fenêtre de suppression d'un contrôle, lancée depuis la fenêtre
 * de menu principal.
 * @author Mickaël Dalbin
 */
public class SupprimerControleController {

    /** Instance de la fenêtre */
    private static SupprimerControleController instance;

    /** Fenêtre */
    private Stage fenetre = new Stage();

    /** Bouton de validation */
    @FXML
    private Button btn_valid;

    /**
     * Crée une instance de la fenêtre d'ajout d'une note
     */
    public SupprimerControleController() {
        super();
    }

    /**
     * @return L'instance de la fenêtre d'ajout d'un contrôle
     */
    public static SupprimerControleController getInstance() {
        if(instance==null) {
            instance = new SupprimerControleController();
        }
        return instance;
    }
    
    /**
     * Lance la fenêtre d'ajout d'un contrôle
     */
    public void showSupprimerControle() {
        initStage();
        fenetre.show();
    }

    /**
     * Initialise la scène à partir du fichier FXML associé
     */
    private void initStage() {  

        try {
              
            AnchorPane pageP;
            
            // chargement du fichier FXML associé
            pageP = FXMLLoader.load(getClass().getResource("SupprimerControle.fxml"));
            
            // on ajoute la boîte à liste à la fenêtre
            pageP.getChildren().add(genererCbx());
            
            // On règle sa position
            pageP.getChildren().get(2).setLayoutX(5.0);
            pageP.getChildren().get(2).setLayoutY(80.0);
            
            // On donne un id à la boîte à liste
            pageP.getChildren().get(2).setId("cbxCtrl");
            
            // Création de la scène et affectation à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // Définition du titre de fenêtre
            fenetre.setTitle("Gestion de notes - Supprimer un contrôle");

        } catch (IOException e) {

            Alerte.affichAlerte(e.getMessage());
        }
    }

    /**
     * Génère la liste déroulante en lui affectant tous les contrôles
     * enregistrés dans l'application.
     * @return une combobox contenant les contrôles
     */
    public static ComboBox<String> genererCbx() {

        // la liste de chaînes de caractères qui sera intégrée à la liste déroulante
        ObservableList<String> listeCtrl = FXCollections.observableArrayList();

        /* Ajout des contrôles à la liste */
        try {
            // chargement des données de l'application
            GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();

            // La liste des UE stockées dans l'application
            ArrayList<UniteEnseignement> listeUE = gdn.getListeSemestres().get(0).getListeUE();

            // Parcours des différentes UE
            for (int i = 0; i < listeUE.size(); i++) {
                
                // liste des modules de l'UE courante
                ArrayList<Module> listeModules = listeUE.get(i).getListeModules();
                
                // Parcours des différents modules
                for (int j = 0; j < listeModules.size(); j++) {
                    
                    // nom du module courant
                    String nomModule = listeModules.get(j).getLibelle();
                    
                    // liste des contrôles du module
                    ArrayList<Controle> listeCtrlCourante = listeModules.get(j).getListeControle();
                    
                    // Parcours de la liste de contrôles du module courant
                    for (int k = 0; k < listeCtrlCourante.size(); k++) {
                        
                        // Contrôle courant
                        Controle ctrlCourant = listeCtrlCourante.get(k);
                        
                        // Ajout du contrôle à la liste de tous les contrôles
                        listeCtrl.add(ctrlCourant.getLibelle() + "  *  "
                                      + ctrlCourant.getDate() + "  *  " + nomModule);
                    }                    
                }
            }

        } catch (ClassNotFoundException e) {
            Alerte.affichAlerte("Problème de chargement des données.");
        }
        
        // On crée un objet de type liste déroulante auquel on affecte la
        // liste des contrôles comme valeurs possibles
        ComboBox<String> cbxControles = new ComboBox<String>(listeCtrl);
        return cbxControles;
    }

    /**
     * Supprime un contrôle après confirmation de l'utilisateur
     * @param event l'événement de type clic
     */
    @FXML
    public void valider(ActionEvent event) {
        
        try {
            // chargement des données de l'application
            GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
            
            // On récupère la boîte à liste de la fenêtre
            ComboBox<String> cbx = (ComboBox<String>) this.btn_valid.getScene().lookup("#cbxCtrl");
            
            // Tableau de chaînes de caractère contenant le choix de l'utilisateur
            String[] controle;
            
            // La chaîne de caractère issue du choix de l'utilisateur
            String choix = cbx.getValue();
            controle = choix.split("\\*");
            
            // Libellé du contrôle
            String libelleCt = controle[0].trim();
            
            // Date du contrôle
            String date = controle[1].trim();
            
            // Libellé du module
            String libelleMod = controle[2].trim();
            
            // Fenêtre de confirmation
            Alert confirmation = Alerte.affichConfirmation("Vous êtes sur le point de supprimer le contrôle\n"
                                                           + libelleCt + " du " + date + " concernant le module\n"
                                                           + libelleMod + "\nSouhaitez-vous continuer ?");
            // On attend la réponse de l'utilisateur
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == ButtonType.OK){
                
                // On supprime le contrôle
                gdn.supprimerControle(libelleMod, libelleCt, date);
                
                // fermeture de la fenêtre
                Stage stage = (Stage) btn_valid.getScene().getWindow();
                stage.close();
            } 
            
        } catch (ClassNotFoundException e) {
            Alerte.affichAlerte("Problème de chargement des données.");
        }
    }
}
