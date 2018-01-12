package application.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Cette classe contient toutes les méthodes concernant un objet 
 * contrôle et permettant de créer cette objet.
 * Un contrôle sera caractérisé par son libellé, sa date, son 
 * coefficient, son module, la liste de toutes les notes obtenus par les 
 * étudiants de la promotion, et facultativement par le nom de l'enseignant.
 * @author Michael Descamps et Mickaël Dalbin
 *
 */
public class Controle implements Serializable{

    /** Exception liée aux contrôles */
    public static class ErreurControle extends Exception {

        /** numéro de version par défaut */
        private static final long serialVersionUID = 1L;

        /**
         * Crée une instance d'une erreur liée à un contrôle
         * @param msg le message associé à l'erreur
         */
        public ErreurControle(String msg) {
            super(msg);
        }

    }

    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;

    /** Libellé du contrôle */
    private String libelle;

    /** Date où le contrôle a été effectué */
    private String date;    

    /** Coefficient du contrôle */
    private double coefficient;

    /** Liste contenant les notes de tout les étudiants */
    private ArrayList<Double> listeNotes;

    /** Le nom par défaut de l'enseignant lorsqu'il n'est pas renseigné */
    private final String nonRenseigne= "NON RENSEIGNÉ";

    /** Nom de l'enseignant ayant réalisé le contrôle */
    private String nomEnseignant;

    /** Module auquel appartient le contrôle */
    private Module module;

    /**
     * Constructeur de l'objet contrôle prenant en argument le nom du fichier
     * contenant les notes, et l'application à laquelle il faut ajouter le
     * contrôle.
     * @param nomFich nom du fichier excel contenant les notes
     * @param gdn l'application à laquelle on ajoute un contrôle
     * @throws ErreurFormatFichierExcel 
     * @throws ClassNotFoundException 
     * @throws ErreurControle 
     */
    public Controle(String nomFich, GestionDeNotes gdn) throws ErreurFormatFichierExcel, ClassNotFoundException, ErreurControle {
        super();
        
        // le tableau qui contiendra les différents éléments contenus dans la ligne d'entête du fichier
        String[] tab;
        
        // tab contient la première ligne du fichier csv
        tab=UtilitaireFichierExcel.premiereLigne(nomFich);
        libelle = tab[1];
        this.date = tab[3];
        this.coefficient = Double.valueOf(tab[2]);
        this.module = gdn.rechercherModule(tab[0].trim());

        // Affectation du nom de l'enseignant
        if (tab.length>4){
            this.nomEnseignant=tab[4];
        }else{
            this.nomEnseignant=nonRenseigne;
        }
        
        if (module != null) {
            // On vérifie si le contrôle n'existe pas déjà
            if (module.controleEstPresent(libelle, date)) {
                throw new ErreurControle("Le contrôle est déjà enregistré.");
            }
            
            /* 
             * vérification de la compatibilité entre la liste d'étudiants du fichier
             * et celle de la promotion associée au contrôle
             */
            ArrayList<String> listeNomsFichiers = UtilitaireFichierExcel.extraireNoms(nomFich);
            ArrayList<String> listeNomsPromo = module.getUe().getSemestre().getPromo().getListeNomsEtudiants();
            if(UtilitaireFichierExcel.listeEgales(listeNomsFichiers, listeNomsPromo)) {
                // On récupère la liste des notes sous forme de chaînes de caractères
                ArrayList<String> listeNotesStr = UtilitaireFichierExcel.verifFichNomNote(nomFich);

                // On la convertit en liste de nombres réels et on l'affecte au contrôle
                this.listeNotes = UtilitaireFichierExcel.convertirListeStringDouble(listeNotesStr) ;            

                // Ajout du contrôle à la liste des contrôles du module
                module.ajouterControle(this);
            } else {
                throw new ErreurControle("La liste du contrôle n'est pas la même que celle de la promotion.");
            }
        } else {
            throw new ErreurControle("Le module associé à ce contrôle n'existe pas.");
        }
    }

    /**
     * @return la date du contrôle
     */
    public String getDate() {
        return date;
    }

    /**
     * @return le coefficient du contrôle
     */
    public double getCoefficient() {
        return coefficient;
    }

    /**
     * @return le nom de l'enseignant
     */
    public String getNomEnseignant() {
        return nomEnseignant;
    }

    /**
     * @return le module associé au contrôle
     */
    public Module getModule() {
        return module;
    }

    /**
     * @return le libellé du contrôle
     */
    public String getLibelle() {
        return libelle;
    }

    /** 
     * @return la liste des notes obtenues par les étudiants
     */
    public ArrayList<Double> getListeNotes() {
        return listeNotes;
    }

    /**
     * Calcule la moyenne obtenue par la promotion sur le contrôle courant
     * @return la moyenne de la promo
     */
    public double calculerMoyenne() {

        double moyenne = 0.0;
        int nbNaN = 0; // compteur de notes qui ne sont pas des réels

        for (int i = 0; i < listeNotes.size(); i++) {
            double noteCourante = listeNotes.get(i);

            if (!Double.isNaN(noteCourante)) {
                moyenne += noteCourante;
            } else {
                nbNaN++;
            }
        }

        if (listeNotes.size() - nbNaN > 0) {
            moyenne = moyenne/(listeNotes.size() - nbNaN);
        }

        return moyenne;
    }
}