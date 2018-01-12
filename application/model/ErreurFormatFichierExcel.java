package application.model;
/*
 * ErreurFormatFichierExcel.java                                                19 oct. 2017
 * IUT info2 2017-2018, pas de droits
 */

/** 
 * Erreur levée lorsqu'un fichier ne répond pas à la présentation attendue
 * @author Michaël Descamps
 */
public class ErreurFormatFichierExcel extends Exception {
    
    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;

    /**
     * Erreur propagée quand le fichier n'est pas présenté de la bonne manière
     * @param message le message d'erreur associé
     */
    public ErreurFormatFichierExcel (String message){
        super(message);
    }   
}
