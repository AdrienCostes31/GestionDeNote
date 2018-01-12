/*
 * GestionDeNotes.java                                   15 déc. 2017
 * IUT info2 2017-2018, pas de droits
 */
package application.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import application.model.Controle.ErreurControle;

/**
 * Classe permettant l'uniformisation des données. Une instance de cette
 * classe contient toutes les données relatives à l'application.
 * @author Mickaël Dalbin
 */
public class GestionDeNotes implements Serializable {
    
    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;
    
    /** Liste des semestres de l'application */
    ArrayList<Semestre> listeSemestres;
    
    /** Identifiant de l'utilisateur */
    private final String identifiant = "admin";
    
    /** Mot de passe de l'utilisateur */
    private String mdp;
    
    /**
     * Constructeur de la classe principale de l'application
     * @throws ClassNotFoundException 
     */
    public GestionDeNotes() throws ClassNotFoundException {
        mdp = "";
        listeSemestres = new ArrayList<Semestre>();
        this.genererSemestre1();
        this.sauvegarderGestionNotes();
    }
    
    /**
     * @return la liste des semestres
     */
    public ArrayList<Semestre> getListeSemestres() {
    	return listeSemestres;
    }
    
    /**
     * @return l'identifiant de l'utilisateur
     */
    public String getIdentifiant() {
        return identifiant;
    }
    
    /**
     * @return le mot de passe de l'utilisateur
     */
    public String getMdp() {
        return mdp;
    }
    
    /**
     * Modifie le mot de passe de l'utilisateur
     * @param newMdp le nouveau mot de passe
     */
    public void setMdp(String newMdp) {
        mdp = newMdp;
    }
    
    /**
     * Ajoute un semestre à la liste des semestres
     * @param semestre le semestre à ajouter
     */
    public void ajouterSemestre(Semestre semestre) {
        listeSemestres.add(semestre);
    }
    
    /**
     * Crée le semestre 1 de la filière informatique, avec ses unités
     * d'enseignement et leurs modules associés.
     * @throws ClassNotFoundException 
     */
    public void genererSemestre1() throws ClassNotFoundException {
        
        // création du semestre
        Semestre semestre1 = new Semestre(1);
        
        // création des UE
        UniteEnseignement ue11 = new UniteEnseignement("Bases de l'informatique", "UE 11", 17.0, semestre1);
        UniteEnseignement ue12 = new UniteEnseignement("Bases de culture scientifique, sociale et humaine ",
                                                       "UE 12", 13.0, semestre1);
        
        // création des modules de l'UE 11
        new Module("Introduction aux systèmes informatiques", "M1101", 3.5, ue11);
        new Module("Introduction à l'algorithmique et à la programmation", "M1102", 3.5, ue11);
        new Module("Structures de données et algorithmes fondamentaux", "M1103", 2.5, ue11);
        new Module("Introduction aux bases de données", "M1104", 3.5, ue11);
        new Module("Conception de documents et d’interfaces numériques", "M1105", 2.5, ue11);
        new Module("Projet tutoré - Découverte", "M1106", 1.5, ue11);
        
        // création des modules de l'UE 12
        new Module("Mathématiques discrètes", "M1201", 2.5, ue12);
        new Module("Algèbre linéaire", "M1202", 2.0, ue12);
        new Module("Environnement économique", "M1203", 1.5, ue12);
        new Module("Fonctionnement des organisations", "M1204", 2.5, ue12);
        new Module("Expression-Communication - Fondamentaux de la communication", "M1205", 2.0, ue12);
        new Module("Anglais et Informatique", "M1206", 1.5, ue12);
        new Module("PPP - Connaître le monde professionnel", "M1206", 1.0, ue12);
        
        // ajout du semestre à l'application
        this.ajouterSemestre(semestre1);
        
        // sauvegarde de l'application
        this.sauvegarderGestionNotes();
    }
    
    /**
     * Recherche un module dans l'application à partir du code de ce module
     * et le renvoie en valeur de retour
     * @param codeModule le code du module recherché
     * @return le module recherché
     */
    public Module rechercherModule(String codeModule) {
        
        // on cherche dans tous les semestres
        for (int i = 0; i < listeSemestres.size(); i++) {
            ArrayList<UniteEnseignement> listeUe = listeSemestres.get(i).getListeUE();
            
            // on cherche dans les UE
            for (int j = 0; j < listeUe.size(); j++) {
                
                ArrayList<Module> listeModules = listeUe.get(j).getListeModules();
                // on cherche dans les modules de l'UE
                for (int k = 0; k < listeModules.size(); k++) {
                    
                    String codeCourant = listeModules.get(k).getCode();
                    if (codeCourant.equals(codeModule)) {
                        // Le module est trouvé
                        return listeModules.get(k);
                    }
                }
            }
        }
        
        // le module n'a pas été trouvé
        return null;
    }
    
    /**
     * Recherche un module dans l'application à partir du libellé de ce module
     * et le renvoie en valeur de retour
     * @param libelleModule le libellé du module recherché
     * @return le module recherché
     */
    public Module rechercherModuleLibelle(String libelleModule) {
        
        // on cherche dans tous les semestres
        for (int i = 0; i < listeSemestres.size(); i++) {
            ArrayList<UniteEnseignement> listeUe = listeSemestres.get(i).getListeUE();
            
            // on cherche dans les UE
            for (int j = 0; j < listeUe.size(); j++) {
                
                ArrayList<Module> listeModules = listeUe.get(j).getListeModules();
                // on cherche dans les modules de l'UE
                for (int k = 0; k < listeModules.size(); k++) {
                    
                    String libelleCourant = listeModules.get(k).getLibelle();
                    if (libelleCourant.equals(libelleModule)) {
                        // Le module est trouvé
                        return listeModules.get(k);
                    }
                }
            }
        }
        
        // le module n'a pas été trouvé
        return null;
    }
    
    /**
     * Affecte une promotion au semestre 1
     * Remplace la promotion précédente
     * @param nomPromo le nom de la promotion à ajouter
     * @param nomFich le nom du fichier où se trouve la promotion
     * @throws ErreurFormatFichierExcel 
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public void affecterPromoS1 (String nomPromo, String nomFich) 
    	throws ClassNotFoundException, IOException, ErreurFormatFichierExcel {
    	
        // réinitialisation du semestre
        listeSemestres.remove(0);
        this.genererSemestre1();
        
        // ajout de la promotion
        new Promotion(nomPromo, nomFich, listeSemestres.get(0));
        
        // sauvegarde des données
        this.sauvegarderGestionNotes();
    }
    
    /**
     * Ajoute un contrôle
     * @param nomFich le nom du fichier contenant le contrôle
     * @throws ErreurFormatFichierExcel 
     * @throws ClassNotFoundException 
     * @throws ErreurControle 
     */
    public void ajouterControle(String nomFich) throws ClassNotFoundException, ErreurFormatFichierExcel, ErreurControle {
        new Controle(nomFich, this);
        this.sauvegarderGestionNotes();
    }
    
    /**
     * Recherche un contrôle précis à partir du libellé de son module,
     * de son libellé et de sa date.
     * @param libModule le libellé du module
     * @param libCtrl le libellé du contrôle
     * @param date la date du contrôle
     * @return le contrôle recherché
     */
    public Controle rechercherControle(String libModule, String libCtrl, String date) {
        
        // Module associé au contrôle à supprimer
        Module moduleAssocie = this.rechercherModuleLibelle(libModule);
        
        if (moduleAssocie != null) {
            
            // la liste des contrôles du module
            ArrayList<Controle> listeCtrl = moduleAssocie.getListeControle();
            
            // parcours de la liste des contrôles
            for (int i = 0; i < listeCtrl.size(); i++) {
                
                // le contrôle courant
                Controle ctrlCourant = listeCtrl.get(i);
                
                // le libellé du contrôle courant
                String libelle = ctrlCourant.getLibelle();
                
                // la date du contrôle courant
                String dateCour = ctrlCourant.getDate();
                
                if (libelle.equals(libCtrl) && dateCour.equals(date)) {
                    // on retourne le contrôle
                    return ctrlCourant;
                }
            }
        }
        
        // Le contrôle n'a pas été trouvé
        return null;
    }
    
    /**
     * Supprime un contrôle 
     * @param libelleModule le libellé du module associé au contrôle à supprimer
     * @param libelleCtrl le libellé du contrôle à supprimer
     * @param date la date du contrôle à supprimer
     */
    public void supprimerControle(String libelleModule, String libelleCtrl, String date) {
        
        // Module associé au contrôle à supprimer
        Module moduleAssocie = this.rechercherModuleLibelle(libelleModule);
        
        if (moduleAssocie != null) {
            // on supprime le contrôle
            moduleAssocie.supprimerControle(libelleCtrl, date);
        }
        this.sauvegarderGestionNotes();
    }
    
    /**
     * Sauvegarde l'état de l'application dans un fichier binaire
     */
    public void sauvegarderGestionNotes() {

        try {
        	
            // création et ouverture du fichier
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/Gestion de Notes"));

            // écriture de l'objet dans le fichier
            try {
                oos.writeObject(this);
            } catch (IOException e) {
                e.printStackTrace(); // problème d'écriture
            }

            // fermeture du fichier
            oos.close();
        } catch (IOException e) {
            e.printStackTrace(); // problème d'accès au fichier
        }
    }
    
    /**
     * Charge l'application à partir du fichier enregistré
     * @return le semestre passé en argument si réussite, null sinon
     * @throws ClassNotFoundException 
     */
    public static GestionDeNotes chargerGestionDeNotes() throws ClassNotFoundException {

        GestionDeNotes gdn = null; // le module à retourner

        try {

            // ouverture du fichier
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/Gestion de Notes"));

            // chargement de l'objet
            gdn = (GestionDeNotes)ois.readObject();

            // fermeture du fichier
            ois.close();

        } catch (IOException e) {
            e.printStackTrace(); // problème d'accès au fichier
        }

        return gdn;
    }
}
