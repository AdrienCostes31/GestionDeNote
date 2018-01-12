package application.view;

import java.io.IOException;
import java.util.ArrayList;

import application.model.Controle;
import application.model.GestionDeNotes;
import application.model.Module;
import application.model.UniteEnseignement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Fenêtre de sélection d'un contrôle pour en afficher la moyenne
 * obtenue par la promotion
 * @author Mickaël Dalbin
 */
public class AfficherMoyenneController {
    
    /** Instance de la fenêtre */
    private static AfficherMoyenneController instance;

    /** Fenêtre */
    private Stage fenetre = new Stage();

    /** Bouton de validation */
    @FXML
    private Button btn_valid;

    /**
     * Crée une instance de la fenêtre de sélection d'un contrôle
     * et d'affichage de la moyenne de la promotion pour celui-ci.
     */
    public AfficherMoyenneController() {
        super();
    }

    /**
     * @return L'instance de la fenêtre
     */
    public static AfficherMoyenneController getInstance() {
        if(instance==null) {
            instance = new AfficherMoyenneController();
        }
        return instance;
    }
    
    /**
     * Lance la fenêtre
     */
    public void showAfficherMoyenne() {
        initStage();
        fenetre.show();
    }

    /**
     * Initialise la fenêtre à partir du fichier FXML associé
     */
    private void initStage() {  

        try {
            // génération de la boîte à liste
            genererCbx();
            
            AnchorPane pageP;
            
            // chargement du fichier FXML associé
            pageP = FXMLLoader.load(getClass().getResource("AfficherMoyenne.fxml"));
            
            // on ajoute la boîte à liste à la fenêtre
            pageP.getChildren().add(genererCbx());
            
            // On règle sa position
            pageP.getChildren().get(2).setLayoutX(5.0);
            pageP.getChildren().get(2).setLayoutY(80.0);
            
            // On donne un id à la boîte à liste
            pageP.getChildren().get(2).setId("cbxCtrl");
            
            // On définit la scène et on l'applique à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // On définit le titre de la fenêtre
            fenetre.setTitle("Gestion de notes - Afficher une moyenne");

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
     * Affiche la moyenne qu'a obtenu la promotion à un contrôle
     * @param event 
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
            
            // On recherche le contrôle
            Controle ctrl = gdn.rechercherControle(libelleMod, libelleCt, date);
            
            // On récupère la moyenne
            double moyenne = ctrl.calculerMoyenne();
            
            // Affichage de la moyenne dans une boîte de dialogue
            Alert info = new Alert(AlertType.INFORMATION);
            info.setResizable(true);
            info.setTitle("Moyenne d'un contrôle");
            info.setContentText("La moyenne de la promotion " + gdn.getListeSemestres().get(0).getPromo().getNom()
                                + " pour le contrôle " + libelleCt + " du " + date + " concernant le module\n"
                                + libelleMod + "\nest de : " + String.valueOf(moyenne));

            info.showAndWait();
                
            // fermeture de la fenêtre
            Stage stage = (Stage) btn_valid.getScene().getWindow();
            stage.close();
            
        } catch (ClassNotFoundException e) {
            Alerte.affichAlerte("Problème de chargement des données.");
        }
    }
}
