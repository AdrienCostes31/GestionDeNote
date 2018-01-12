/*
 * Promotion.java                                   16 nov. 2017
 * IUT info2 2017-2018 groupe 1, pas de droits
 */
package application.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;

/** 
 * Promotion d'étudiants de l'IUT. Elle est caractérisée par son nom
 * et le semestre auquel elle est rattachée
 * @author Adrien Costes et Mickaël Dalbin
 */
public class Promotion implements Serializable {

    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;

    /** Nom de la promotion */
    private String nom;
    
    /** Nombre d'étudiant de la promotion */
    private int nbEtud;
    
    /** Semestre associé à la promotion */
    private Semestre semestre;
    
    /** Liste des étudiants */
    private ArrayList<Etudiant> listeEtudiant;
    
    /** Liste des noms des étudiants */
    private ArrayList<String> listeNomsEtudiants;
    
    /**
     * Expression régulière qui correspond à un nom complet.
     * Le nom peut débuter par un ou plusieurs espaces (facultatif)
     * Ensuite on doit trouver une lettre majuscule
     * Puis ensuite, éventuellement, doivent se succéder des lettres (minuscules
     * ou majuscules, des traits d'union, des apostrophes. Ces caractères peuvent
     * être séparés par un ou plusieurs espaces. Tous les caractères accentués
     * sont pris en compte.
     */
    private static final String REGEX_NOM = " *[A-ZÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ][a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ'\\- ]*"; 
    
    /** 
     * Crée un instance de promotion
     * @param nom le nom de la promotion
     * @param nomFichierEtudiants le fichier qui contient la liste des étudiants
     * @param semestre le semestre associé à la promotion
     * @throws IOException 
     * @throws ErreurFormatFichierExcel 
     */
    public Promotion(String nom, String nomFichierEtudiants, Semestre semestre) 
                     throws IOException, ErreurFormatFichierExcel {
        this.nom = nom;
        listeEtudiant = new ArrayList<Etudiant>();
        
        // remplissage de la liste d'étudiants à partir du fichier
        geneListeEtud(nomFichierEtudiants);
        nbEtud = listeEtudiant.size();
        
        // remplissage de la liste des noms
        listeNomsEtudiants = UtilitaireFichierExcel.extraireNomsEtudiants(listeEtudiant);
        
        // On affecte la promotion au semestre
        semestre.setPromo(this);
        this.semestre = semestre;
    }

    /**
     * @return le nom de la promotion
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return la liste des étudiants
     */
    public ArrayList<Etudiant> getListeEtudiant() {
        return listeEtudiant;
    }

    /**
     * @return le nombre d'étudiants
     */
    public int getNbEtud() {
        return nbEtud;
    }
    
    /**
     * @return le semestre associé à la promotion
     */
    public Semestre getSemestre() {
        return semestre;
    }
    
    /**
     * @return la liste des noms des étudiants
     */
    public ArrayList<String> getListeNomsEtudiants() {
        return listeNomsEtudiants;
    }
    
    /**
     * récupération de la liste des étudiants dans un fichier excel
     * @param nomFichier le nom du fichier excel contenant la promotion
     * @throws IOException 
     * @throws ErreurFormatFichierExcel 
     */
    public void geneListeEtud(String nomFichier) throws IOException, ErreurFormatFichierExcel {
        
        /*variable qui contiendra une ligne du fichier*/
        String ligne = "";
        
        /*ligne du fichier*/
        String[] lignef;
        
        /*nom*/
        String[] nom;
        
        try ( BufferedReader fichier = new BufferedReader(new FileReader(nomFichier))) {
            
            ligne = fichier.readLine();
            
            // Si la première ligne ne contient pas un nom, on lance une erreur de format de fichier
            if (!ligne.matches(REGEX_NOM)) {
                throw new ErreurFormatFichierExcel("Le fichier ne répond pas à "
                                                   + "la présentation attendue.");
            }
            
            do {
                /*prénom de l'étudiant*/
                String prenom;
                
                /* nom de famille de l'étudiant */
                String nomFamille = "";
                
                // On isole chaque colonne
                lignef = ligne.split(";");
                
                // On sépare le nom du prénom, ce dernier sera contenu dans
                // la dernière case du tableau nom[]
                nom = lignef[0].split(" ");
                    
                // on concatène les chaînes pour obtenir le nom complet de
                // l'étudiant en cas de nom en plusieurs mots
                if (nom.length > 2) {
                    for(int i = 0; i < nom.length - 1; i++) {
                        nomFamille += nom[i] + ' ';
                    }
                    // On retire le dernier caractère espace
                    nomFamille = nomFamille.substring(0, nomFamille.length()-1);
                } else {
                    nomFamille = nom[0];
                }
                
                // le prénom est la dernière chaîne du tableau
                prenom = nom[nom.length - 1];
                
                // On ajoute l'étudiant à la liste des étudiants
                Etudiant etudiant = new Etudiant(nomFamille, prenom, this);
                listeEtudiant.add(etudiant);     

                ligne = fichier.readLine();           
            } while (ligne != null);
            
            fichier.close();
            
        } catch (IOException e) {
            // géré dans AjouterPromotionController.java
        }
    }
    
    /**
     * Génère tous les relevés des étudiants de la promotion pour le semestre
     * @throws DocumentException 
     */
    public void genererTousReleves() throws DocumentException {
        
        // On parcours la liste des étudiants de la promo et on génère tous les relevés
        for (int i = 0; i < listeEtudiant.size(); i++) {
            Etudiant etCourant = listeEtudiant.get(i);
            etCourant.genererReleve();
        }
    }
}