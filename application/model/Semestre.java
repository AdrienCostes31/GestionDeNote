package application.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Semestre d'étude d'une filière de l'IUT, caractérisé par son numéro.
 * Une promotion au plus peut lui être associé.
 * @author Michaël Descamps et Mickaël Dalbin
 *
 */
public class Semestre implements Serializable {

    /** Numéro de version de la classe */
    private static final long serialVersionUID = 1L;

    /** Numéro du semestre */
    private int nbSemestre;

    /** Nom du semestre */
    private String nom;

    /** Liste des UE contenus dans le semestre */
    private ArrayList<UniteEnseignement> listeUE;
    
    /** Promotion associée au semestre */
    private Promotion promo;

    /**
     * Crée une instance de semestre à partir de son numéro
     * @param nb le numéro du semestre
     */
    public Semestre(int nb) {
        super();
        this.nbSemestre=nb;
        nom = "Semestre " + nbSemestre;
        listeUE= new ArrayList<UniteEnseignement>();
    }
    
    /**
     * @return la promotion associée au semestre courant
     */
    public Promotion getPromo() {
        return promo;
    } 
   
    /**
     * Associe une promotion au semestre courant
     * @param promo la promotion à associer
     */
    public void setPromo(Promotion promo) {
        this.promo = promo;
    }
    
    /**
     * @return le nom du semestre
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the listeUE
     */
    public ArrayList<UniteEnseignement> getListeUE() {
        return listeUE;
    }

    /** 
     * Ajouter une UE dans la liste des UE
     * @param UE l'UE à ajouter
     */
    public void ajouterUE(UniteEnseignement UE){
        listeUE.add(UE);
    }
}
