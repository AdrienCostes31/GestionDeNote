package application.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import application.model.ErreurFormatFichierExcel;
import application.model.GestionDeNotes;
import application.model.Controle.ErreurControle;
import javafx.event.ActionEvent;

/**
 * Fenêtre contextuelle d'ajout d'un contrôle lancée depuis la fenêtre
 * de menu principal de l'application.
 * @author Mickaël Dalbin
 */
public class AjouterNoteController {

    /** Instance de la fenêtre */
    private static AjouterNoteController instance;

    /** Scène de cette fenêtre */
    private Stage fenetre = new Stage();

    /** Champ de texte destiné au chemin du fichier du contrôle à ajouter */
    @FXML
    private TextField nomFich;

    /** Bouton de validation */
    @FXML
    private Button btn_valid;

    /** Bouton de parcours pour la recherche de fichier */
    @FXML
    private Button btn_parcour;

    /** Explorateur de fichier */
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Crée une instance de la fenêtre d'ajout d'une note
     */
    public AjouterNoteController() {
        super();
    }

    /**
     * @return L'instance de la fenêtre d'ajout d'un contrôle
     */
    public static AjouterNoteController getInstance() {
        if(instance==null) {
            instance = new AjouterNoteController();
        }
        return instance;
    }

    /**
     * Lance la fenêtre d'ajout d'un contrôle
     */
    public void showAjouterNote() {
        initStage();
        fenetre.show();
    }

    /**
     * Initialise la fenêtre à partir du fichier FXML associé
     */
    private void initStage() {  

        try {

            Parent pageP;
            // chargement du fichier FXML associé
            pageP = FXMLLoader.load(getClass().getResource("AjouterNote.fxml"));
            
            // On crée la scène et on l'affecte à la fenêtre
            Scene page = new Scene(pageP);
            fenetre.setScene(page);
            
            // On définit le titre de la fenêtre
            fenetre.setTitle("Gestion de notes - Ajout d'un contrôle");

        } catch (IOException e) {

            Alerte.affichAlerte("Impossible de charger la page.");
        }
    }

    /**
     * Lance une fenêtre de confirmation avant d'ajouter ou non le contrôle
     * suivant la réponse de l'utilisateur
     * @param event l'événement de type clic
     */
    @FXML
    public void valider(ActionEvent event) {

        // On contrôle que les deux champs soient non vides
        if (nomFich.getText().isEmpty()) {
            Alerte.affichAlerte("Veuillez sélectionner un fichier.");
        } else {
            try {
                // chargement des données de l'application
                GestionDeNotes gdn = GestionDeNotes.chargerGestionDeNotes();
                
                // ajout du contrôle
                gdn.ajouterControle(nomFich.getText());
                
                // fermeture de la fenêtre
                Stage stage = (Stage) btn_valid.getScene().getWindow();
                stage.close();
                
            } catch (ClassNotFoundException e) {
                Alerte.affichAlerte("Impossible de charger les données.");
            } catch (ErreurFormatFichierExcel e) {
                Alerte.affichAlerte(e.getMessage());
            } catch (ErreurControle e) {
                Alerte.affichAlerte(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                Alerte.affichAlerte("Le fichier choisi n'est pas compatible.");
            }
        }
    }

    /**
     * Configure l'explorateur de fichier
     * @param fileChooser l'explorateur à configurer
     */
    private static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("Navigateur de fichier");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
    }

    /**
     * Action lorsque l'utilisateur clique sur le bouton "Parcourir..."
     * @param event l'événement de type clic
     */
    @FXML
    public void parcourFichier(ActionEvent event) {
        
        // configuration de l'explorateur de fichiers
        configureFileChooser(fileChooser);
        
        // ouvre la fenêtre et renvoie une instance de fichier
        File file = fileChooser.showOpenDialog(fenetre);

        try {
            // On récupère le chemin du fichier
            nomFich.setText(file.getAbsolutePath());
        } catch (Exception e) {
            // aucun traitement
        }
    }
}
