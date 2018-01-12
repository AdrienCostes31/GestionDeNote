/*
 *Module.java								14 nov. 2017
 *IUT de Rodez 
 */
package application.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Module d'enseignement caractérisé par son libellé, son code, son 
 * coefficient et son unité d'enseignement associée.
 * @author Adrien Costes et Mickaël Dalbin
 */
public class Module implements Serializable {

    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;

    /** Libellé complet du module */
    private String libelle;
    
    /** Code unique du module commençant par 'M' et suivi de 5 chiffres */
    private String code;

    /** Coefficient du module */
    private double coef;

    /** UE à laquelle est rattaché le module */
    private UniteEnseignement ue;

    /** Liste des contrôles contenus dans le module */
    private ArrayList<Controle> listeControle;

    /**
     * Construit un module à partir de son libellé, son code, son coefficient
     * et de l'UE associée
     * @param libelle le libellé complet du module
     * @param code le code unique du module
     * @param coef le coefficient du module
     * @param ue l'UE à laquelle est rattachée le module
     * @throws ClassNotFoundException 
     */
    public Module(String libelle, String code, double coef, UniteEnseignement ue) throws ClassNotFoundException {
        this.libelle = libelle;
        this.code = code;
        this.coef = coef;
        listeControle = new ArrayList<Controle>();
        
        // ajout du module à l'UE
        ue.ajouterModule(this);
        this.ue = ue;
    }

    /**
     * @return le libellé du module
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return le coefficient du module
     */
    public double getCoef() {
        return coef;
    }

    /**
     * @return le code du module
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @return l'UE associée au module
     */
    public UniteEnseignement getUe() {
        return ue;
    }

    /**
     * @return la liste des contrôles
     */
    public ArrayList<Controle> getListeControle() {
        return listeControle;
    }
    
    /**
     * Ajoute un contrôle à la liste des contrôles après avoir vérifié
     * qu'il n'était pas déjà présent
     * @param ctrl le contrôle à ajouter
     */
    public void ajouterControle(Controle ctrl) {
        if (!controleEstPresent(ctrl.getLibelle(), ctrl.getDate())) {
            listeControle.add(listeControle.size(),ctrl);
        }
    }
    
    /**
     * Évalue si un contrôle est présent dans la liste des contrôles
     * à partir de son libellé et de sa date
     * @param libelle le libellé du contrôle à rechercher
     * @param date la date du contrôle à rechercher
     * @return true si le contrôle est déjà présent
     *         false sinon
     */
    public boolean controleEstPresent(String libelle, String date) {
        
        // parcours de la liste des contrôles
        for (int i = 0; i < listeControle.size(); i++) {
            
            // le contrôle courant
            Controle ctrlCourant = listeControle.get(i);
            // Libellé du contrôle courant
            String lblCtrl = ctrlCourant.getLibelle();
            // Date du contrôle courant
            String dateCtrl = ctrlCourant.getDate();
            
            if (lblCtrl.equals(libelle) && dateCtrl.equals(date)) {
                return true;
            }
        }
        
        // le contrôle est absent de la liste
        return false;
    }
    
    /**
     * Supprime un contrôle de la liste des contrôles s'il est présent
     * @param libelle le libellé du contrôle à supprimer
     * @param date la date du contrôle à supprimer
     */
    public void supprimerControle(String libelle, String date) {
        
        // parcours de la liste des contrôles
        for (int i = 0; i < listeControle.size(); i++) {
            
            // le contrôle courant
            Controle ctrlCourant = listeControle.get(i);
            // Libellé du contrôle courant
            String lblCtrl = ctrlCourant.getLibelle();
            // Date du contrôle courant
            String dateCtrl = ctrlCourant.getDate();
            
            if (lblCtrl.equals(libelle) && dateCtrl.equals(date)) {
                listeControle.remove(i);
            }
        }
    }
}
