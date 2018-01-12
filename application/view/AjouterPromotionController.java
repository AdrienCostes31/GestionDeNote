package application.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import application.model.ErreurFormatFichierExcel;
import application.model.GestionDeNotes;
import javafx.event.ActionEvent;

/**
 * Fenêtre d'ajout d'une promotion, lancée depuis le menu principal
 * de l'application.
 * @author Adrien Costes et Mickaël Dalbin
 */
public class AjouterPromotionController{

    /** Fenêtre */
    Stage fenetre = new Stage();

    /** Champ de texte contenant le chemin absolu vers le fichier */
    @FXML
    private TextField nomFich;
    
    /** Bouton de validation */
    @FXML
    private Button btn_valid;
    
    /** Bouton de parcours de l'explorateur de fichier */
    @FXML
    private Button btn_parcour;
    
    /** Champ de texte contenant le nom à affecter à la promotion */
    @FXML
    private TextField nomPromo;

    /** Instance de la fenêtre */
    private static AjouterPromotionController instance;

    /** Explorateur de fichiers */
    private final FileChooser fileChooser = new FileChooser(); // le File Chooser

    /**
     * @return l'instance de la fenêtre
     */
    public static AjouterPromotionController getInstance() {
        if(instance==null) {
            instance = new AjouterPromotionController();
        }
        return instance;
    }

    /**
     * Crée une instance de la fenêtre
     */
    public AjouterPromotionController() {
        super();
    }
    
    /**
     * Lance la fenêtre d'ajout de promotion
     */
    public void showAjouterPromotion() {
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
            pageP = FXMLLoader.load(getClass().getResource("AjouterPromotion.fxml"));
            
            // Création de la scène et affectation à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // Définition du titre de la fenêtre
            fenetre.setTitle("Gestion de notes - Ajout d'une promotion");

        } catch (IOException e) {

            Alerte.affichAlerte("Impossible de charger la fenêtre.");
        }
    }

    /**
     * Configure l'explorateur de fichiers
     * @param fileChooser
     */
    private static void configureFileChooser(final FileChooser fileChooser){                           
        
        // choix du titre de fenêtre
        fileChooser.setTitle("Navigateur de fichier");
        
        // choix du répertoire initial
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
    }

    /**
     * Ouvre l'explorateur de fichiers lorsque le bouton "Parcourir..." est
     * cliqué.
     * @param event l'événement de type clic
     */
    @FXML
    public void parcourFichier(ActionEvent event) {
        
        // Configuration de l'explorateur de fichiers
        configureFileChooser(fileChooser);
        
        // ouvre la fenêtre et renvoi une instance de l'explorateur
        File file = fileChooser.showOpenDialog(fenetre); 
        
        try {
            // On récupère le chemin du fichier
            nomFich.setText(file.getAbsolutePath());
        } catch (Exception e) {
            // aucun traitement
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
        if (nomFich.getText().isEmpty() || nomPromo.getText().isEmpty()) {
            Alerte.affichAlerte("Un des deux champs est vide.");
        } else {
            try {
                // On charge les données de l'application
                GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
                
                // On vérifie s'il y a déjà une promotion enregistrée
                if (gdn.getListeSemestres().get(0).getPromo() != null) {
                    
                    Alert alerte = Alerte.affichConfirmation("Une promotion existe déjà pour ce semestre.\n"
                            + "Voulez-vous vraiment la remplacer ?");
                    
                    // On attend la réponse de l'utilisateur
                    Optional<ButtonType> result = alerte.showAndWait();
                    
                    if (result.get() == ButtonType.OK){
                        
                        // On remplace la promotion existante par la nouvelle
                        gdn.affecterPromoS1(nomPromo.getText(), nomFich.getText());
                        
                        // fermeture de la fenêtre
                        Stage stage = (Stage) btn_valid.getScene().getWindow();
                        stage.close();
                    } 
                    
                } else {
                    
                    // On affecte la promotion au semestre 1
                    gdn.affecterPromoS1(nomPromo.getText(), nomFich.getText());

                    // fermeture de la fenêtre
                    Stage stage = (Stage) btn_valid.getScene().getWindow();
                    stage.close();
                }
            } catch (ErreurFormatFichierExcel e) {
                Alerte.affichAlerte(e.getMessage());
            } catch (IOException e) {
                Alerte.affichAlerte("Problème d'accés au fichier");
            } catch (ClassNotFoundException e) {
                Alerte.affichAlerte("Le chargement de l'application a échoué.");
            }
        }		
    } 
}


