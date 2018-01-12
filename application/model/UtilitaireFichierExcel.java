package application.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import application.view.Alerte;

/*
 * UtilitaireFichierExcel.java                                                19 oct. 2017
 * IUT info2 2017-2018, pas de droits
 */

/**
 * Classe utilitaire contenant des méthodes utilisées dans d'autres classes.
 * @author Michael Descamps et Mickaël Dalbin
 */
public class UtilitaireFichierExcel  {

    /**
     * Expression régulière qui correspond a la ligne 7 contenant uniquement l'
     * intitulé de la colonne censée contenir "nom..." 
     */
    private static final String REGEX_LIGNE_NOM = " *((N|n)(O|o)(m|M)(S|s)?) *([a-zA-Z]* *)* *;.*";

    /** Expression régulière qui correspond au contenu valide pour la première
     * colonne du fichier Excel, supposée contenir le nom et éventuellement prénom
     * d'une personne.
     * La colonne peut débuter par un ou plusieurs espaces (facultatif)
     * Ensuite on doit trouver une lettre majuscule
     * Puis ensuite, éventuellement, doivent se succéder des lettres (minuscules
     * ou majuscules, des traits d'union, des apostrophes. Ces caractères peuvent
     * être séparés par un ou plusieurs espaces.
     */
    private static final String REGEX_COLONNE_NOM = " *[A-ZÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ][a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ'\\- ]*"; 

    /**
     * Expression régulière qui correspond à une note : entier compris entre 0 et 20
     * ou la mention "ABS"
     */
    private static final String REGEX_COLONNE_NOTE = "( *((1[0-9])|20|[0-9]) *)|( *((A|a)(b|B)(S|s)) *)"; 
    
    /**
     * Expression régulière correspondant à la mention "ABS"
     */
    private static final String REGEX_ABS = "( *((A|a)(b|B)(S|s)) *)";

    /**
     * expression régulière permettant de vérifier  qu'une chaîne de caractère correspond
     * a un module
     */
    private static final String REGEX_MODULE = " *M[0-9]{4}C? *"; 

    /**
     * expression régulière permettant de vérifier  qu'une chaîne de caractère correspond
     * a un coefficient
     */
    private static final String REGEX_COEFFICIENT = " *\\d\\.?\\d? *"; 

    /**
     * expression régulière permettant de vérifier  qu'une chaîne de caractère correspond
     * a une date
     */
    private static final String REGEX_DATE = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)"; 

    /**indique si deux liste de chaînes de caractères 
     * sont égales
     * @param listeA 
     * @param listeB 
     * @return true si bon ordre
     *         false sinon
     * 
     * 
     */
    public static boolean listeEgales(ArrayList<String> listeA,ArrayList<String> listeB){
        if (listeA.size()!=listeB.size()){
            return false;
        }
        for (int i=0;i<listeA.size();i++){
            if (!(listeA.get(i).trim().equals(listeB.get(i).trim()))){
                return false;
            }  
        }
        return true;
    }

    /** 
     * indique si une liste est trié dans l'ordre lexicographique ou non
     * @param liste la liste qui contient les noms
     * @return true si bon ordre
     *         false sinon
     * 
     * 
     */
    public static boolean estTrie(ArrayList<String> liste){

        for (int i=1;i<liste.size();i++){
            if (liste.get(i-1).trim().compareTo(liste.get(i).trim())>0){
                return false;
            }  
        }
        return true;
    }

    /**
     * Examine un fichier de notes et extrait la liste des noms des étudiants
     * @param nomFich le nom du ficher à examiner
     * @return une ArrayList de nombres réels contenant les noms des étudiants
     * @throws ErreurFormatFichierExcel 
     */
    public static ArrayList<String> extraireNoms(String nomFich) throws ErreurFormatFichierExcel {

        String ligne=""; 
        String[] tabChaine;
        ArrayList<String> listeNoms = new ArrayList<String>();
        int numLigne=0;



        try ( BufferedReader fichier=new BufferedReader(new FileReader(nomFich))){

            /* On vérifie que le fichier soit bien présenté et qu'il contienne 
             * une liste de noms
             */
            do{
                ligne=fichier.readLine();
                numLigne++;
            }while(ligne!=null && !ligne.matches(REGEX_LIGNE_NOM));

            if (ligne==null){
                throw new ErreurFormatFichierExcel("Aucune ligne "
                        + "ne contient la string \"nom\" ");
            }

            ligne=fichier.readLine();
            numLigne++;

            do{
                tabChaine= ligne.split(";"); // tabChaine contient un nom et une note
                if ( tabChaine.length<2){
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro" 
                            + " %s  contient moins de 2 colonnes", numLigne)); 
                }else if( !tabChaine[0].matches(REGEX_COLONNE_NOM)){
                    System.out.println(tabChaine[0]);
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro"
                            + " %s  ne contient pas un nom en premiere colonne", numLigne));
                }else if(!tabChaine[1].matches(REGEX_COLONNE_NOTE)){
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro" 
                            + " %s  ne contient pas une note en deuxième colonne", numLigne)); 
                }
                listeNoms.add(tabChaine[0]);
                ligne=fichier.readLine();
                numLigne++;
            }while(ligne!=null && !ligne.equals(";"));
        }catch(IOException e){
            Alerte.affichAlerte("Problème d'accès aux fichiers.");
        }
        return listeNoms;
    }

    /** 
     * Examine un fichier contenant les notes d'un contrôle et retourne une ArrayList
     * de chaînes de caractères contenant les notes
     * @param nomFich nom du fichier a lire
     * @return  la liste des notes sous forme d'une liste de chaînes de caractères
     * @throws ErreurFormatFichierExcel 
     * 
     */
    public static ArrayList<String> verifFichNomNote(String nomFich) throws ErreurFormatFichierExcel{


        String ligne=""; 
        String[] tabChaine;
        ArrayList<String> listeNote= new ArrayList<String>();
        int numLigne=0;



        try ( BufferedReader fichier=new BufferedReader(new FileReader(nomFich))){

            do{
                ligne=fichier.readLine();
                numLigne++;
            }while(ligne!=null && !ligne.matches(REGEX_LIGNE_NOM));

            if (ligne==null){
                throw new ErreurFormatFichierExcel("Aucune ligne "
                        + "ne contient la string \"nom\" ");
            }

            ligne=fichier.readLine();

            do{
                numLigne++;
                tabChaine= ligne.split(";"); // tabChaine contient un nom et une note
                if ( tabChaine.length<2){
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro" 
                            + " %s  contient moins de 2 colonnes", numLigne)); 
                }else if( !tabChaine[0].matches(REGEX_COLONNE_NOM)){
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro"
                            + " %s  ne contient pas un nom en premiere colonne", numLigne));
                }else if(!tabChaine[1].matches(REGEX_COLONNE_NOTE)){
                    throw new ErreurFormatFichierExcel(String.format("la ligne significative numéro" 
                            + " %s  ne contient pas une note en deuxième colonne", numLigne)); 
                }
                listeNote.add(tabChaine[1]);
                ligne=fichier.readLine();
            }while(ligne!=null);
        }catch(IOException e){
            // sera géré par l'interface
        }
        return listeNote;
    }
    
    /**
     * Permet de transformer une liste de notes sous forme de chaînes de caractères
     * en une liste de notes sous forme de nombres réels.
     * Lorsque l'étudiant a été absent, la valeur NaN est stockée
     * @param listeString la liste contenant les notes sous forme de chaînes de caractères
     * @return la liste sous forme de nombres réels
     */
    public static ArrayList<Double> convertirListeStringDouble(ArrayList<String> listeString) {
        
        // la liste de notes sous forme de nombres réels
        ArrayList<Double> listeDouble = new ArrayList<Double>();
        
        // Parcours de la liste de chaînes de caractères
        for (int i = 0; i < listeString.size(); i++) {
            String noteCouranteStr = listeString.get(i);
            double noteCouranteDbl;
            
            // On teste si la valeur est un nombre réel
            if (noteCouranteStr.matches(REGEX_ABS)) {
                noteCouranteDbl = Double.NaN;
                // on insère la note dans la liste
                listeDouble.add(noteCouranteDbl);
            } else {
                noteCouranteDbl = Double.valueOf(noteCouranteStr);
                // on insère NaN si la valeur n'est pas un nombre réel
                listeDouble.add(noteCouranteDbl);
            }
        }        
        
        return listeDouble;        
    }

    /**
     * méthode permettant de recuperer la 1ere ligne 
     * d'un fichier csv censée contenir le code du module ,le nom, le coefficient,la date,
     * l'enseignant (facultatif) du controle
     * @param nomFich nom du fichier excel
     * @return tabChaine tableau contenant les différents éléments de l'en-tête
     * @throws ErreurFormatFichierExcel 
     */
    public static String[] premiereLigne (String nomFich) throws ErreurFormatFichierExcel{
        String ligne=""; 
        String[] tabChaine=null;



        try ( BufferedReader fichier=new BufferedReader(new FileReader(nomFich))){

            ligne=fichier.readLine();
            if (ligne==null){
                throw new ErreurFormatFichierExcel("Le fichier Excel" 
                        + " ne contient pas la  première lignes d'en-tête");  
            }

            tabChaine= ligne.split(";"); // ligne est censé contenir la ligne avec "Nom"
            if (!tabChaine[0].matches(REGEX_MODULE)){
                throw new ErreurFormatFichierExcel("Le module est manquant ou invalide"); 
            }else if (tabChaine[1].equals(null)){
                throw new ErreurFormatFichierExcel("Le libellé du contrôle est manquant ou invalide"); 
            }else if (!tabChaine[2].matches(REGEX_COEFFICIENT)){
                throw new ErreurFormatFichierExcel("Le coefficient du contrôle est manquant ou invalide");  
            }else if (!tabChaine[3].matches(REGEX_DATE)){
                throw new ErreurFormatFichierExcel("La date du contrôle est manquant ou invalide"); 
            }
        } catch(IOException e){
            // sera géré par l'interface 
        } catch(ErreurFormatFichierExcel e) {
            // sera géré par l'interface
        } catch(NumberFormatException e) {
            throw new ErreurFormatFichierExcel("Les nombres à virgule doivent être écrits avec \".\"");
        }
        return tabChaine;
    }
    
    /**
     * A partir d'une liste d'objets Etudiant, extrait le nom et le prénom
     * pour renvoyer une liste de chaînes de caractères
     * @param listeEtudiants la liste contenant les étudiants
     * @return une liste de chaînes de caractères contenant les noms des étudiants
     */
    public static ArrayList<String> extraireNomsEtudiants(ArrayList<Etudiant> listeEtudiants) {
        
        ArrayList<String> listeNoms = new ArrayList<String>();
        String nom,
               prenom;
        
        for (int i = 0; i < listeEtudiants.size(); i++) {
            nom = listeEtudiants.get(i).getNom();
            prenom = listeEtudiants.get(i).getPrenom();
            listeNoms.add(nom + " " + prenom);
        }
        
        return listeNoms;
    }
}
