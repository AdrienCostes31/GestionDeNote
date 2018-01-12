package application.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Unité d'enseignement d'un DUT. Elle est associée à un semestre et 
 * est sous-divisée en modules. Elle est caractérisée par son nom, son code
 * et son coefficient.
 * @author Michael Descamps et Mickaël Dalbin
 *
 */
public class UniteEnseignement implements Serializable {

    /** Numéro de version de la classe */
    private static final long serialVersionUID = 1L;

    /** Nom de l'UE */
    private String nom;

    /** Code associé à l'UE */
    private String code;
    
    /** Coefficient total de l'UE */
    private double coefTotal;

    /** Semestre dans lequel est enseigné cette UE */
    private Semestre semestre;

    /** Liste des modules contenus dans l'UE */
    private ArrayList<Module> listeModules;

    /**
     * Crée une instance de l'unité d'enseignement
     * @param nom le nom complet de l'UE
     * @param code le code unique associé à l'UE
     * @param coef le coefficient total de l'UE
     * @param semestre le semestre associé à l'UE
     * @throws ClassNotFoundException 
     */
    public UniteEnseignement(String nom, String code, double coef, Semestre semestre)
           throws ClassNotFoundException {
        super();
        this.nom = nom;
        this.code = code;
        this.coefTotal = coef;
        this.listeModules= new ArrayList<Module>();
        
        // ajout de l'UE au semestre
        semestre.ajouterUE(this);
        this.semestre = semestre;
    }

    /**
     * @return le nom de l'UE
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le semestre associé
     */
    public Semestre getSemestre() {
        return semestre;
    }
    
    /**
     * @return le code de l'UE
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @return le coefficient total de l'UE, c'est-à-dire la somme des coefficients de ses modules
     */
    public double getCoefTotal() {
        return coefTotal;
    }
    
    /**
     * @return la liste des modules
     */
    public ArrayList<Module> getListeModules() {
        return listeModules;
    }

    /**
     * permet d'ajouter un module àl'UE
     * @param module le module a ajouter
     */
    public void ajouterModule(Module module){
        listeModules.add(module);
    }
}
