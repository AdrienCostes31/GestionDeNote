package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Classe principale de lancement de l'application
 * @author Adrien Costes
 */
public class Principale extends Application {

    /** Fenêtre */
    private Stage primaryStage;
    
    /** Structure de fenêtre */
    private BorderPane rootLayout;
    

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Identification");

        initRootLayout();

        showIdentification();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la fenêtre d'identification
     */
    public void showIdentification() {
        try {
            // Chargement du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/Identification.fxml"));
            AnchorPane identification = (AnchorPane) loader.load();

            // Positionnement de la fenêtre
            rootLayout.setCenter(identification);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Méthode principale de lancement de l'application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
