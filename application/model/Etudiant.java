/*
 *Etudiant.java                                                         14 nov. 2017
 *IUT de Rodez 
 */
package application.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Etudiant de l'IUT caractérisé par son nom, son prénom et sa promotion.
 * @author Adrien Costes et Mickaël Dalbin
 */
public class Etudiant implements Serializable {

    /** Numéro de version par défaut */
    private static final long serialVersionUID = 1L;

    /** Nom de l'étudiant S*/
    private String nom;

    /** Prénom de l'étudiantS */
    private String prenom;
    
    /** Promotion de l'étudiant */
    private Promotion promo;

    /**
     * Constructeur permettant de créer un étudiant grâce à son nom, son prénom
     * et sa promotion
     * @param nom le nom de l'étudiant
     * @param prenom le prénom de l'étudiant
     * @param promo la promotion de l'étudiant
     */
    public Etudiant(String nom, String prenom, Promotion promo) {
        this.nom = nom;
        this.prenom = prenom;
        this.promo = promo;
    }


    /**
     * @return le nom de l'étudiant
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le prénom de l'étudiant
     */
    public String getPrenom() {
        return prenom;
    }
    
    /**
     * @return la promotion de l'étudiant
     */
    public Promotion getPromo() {
        return promo;
    }
    
    /**
     * Recherche l'indice auquel est positionné l'étudiant courant
     * dans la liste des étudiants de sa promotion
     * @return l'indice
     */
    public int getIndicePromo() {
        
        int erreur = -1; // indice invalide si l'étudiant n'est pas dans la liste
        ArrayList<String> listeNoms = promo.getListeNomsEtudiants();
        
        for (int i = 0; i < listeNoms.size(); i++) {
            
            // si le nom complet est identique
            if (listeNoms.get(i).equals(nom + " " + prenom)) {
                return i;
            }
        }
        
        return erreur;
    }
    
    /**
     * Calcule la moyenne obtenue par l'étudiant sur un module
     * @param module le module concerné
     * @return la moyenne de l'étudiant sur le module
     */
    public double calculerMoyenneModule(Module module){
        
        double moyenne = 0.0;
        double sommeCoeff = 0.0;
        int indiceEtudiant = getIndicePromo(); // L'indice de l'étudiant dans la liste des étudiants
        ArrayList<Controle> listeControle = module.getListeControle();
        
        for (int i = 0; i < listeControle.size(); i++) {
            // la note obtenue pour le ième contrôle
            double note = listeControle.get(i).getListeNotes().get(indiceEtudiant);
            // le coefficient du ième contrôle
            double coef = listeControle.get(i).getCoefficient();
            
            /*
             *  on fait la moyenne des notes obtenues lors des contrôles
             *  de ce module où l'étudiant était présent
             */
            if (!Double.isNaN(note)) {
                moyenne += note * coef;
                sommeCoeff += coef;
            }
        }
        
        if (sommeCoeff > 0) {
            // on divise par le nombre de contrôles
            moyenne = moyenne/sommeCoeff;
        }
        
        return moyenne;
    }
    
    /**
     * Calcule la moyenne obtenue par l'étudiant sur une unité d'enseignement
     * @param ue l'unité d'enseignement concernée
     * @return la moyenne de l'étudiant sur l'unité d'enseignement
     */
    public double calculerMoyenneUe(UniteEnseignement ue){
        
        double moyenne = 0.0;
        double sommeCoeff = 0.0;
        // on récupère la liste des modules associés à l'UE
        ArrayList<Module> listeModule = ue.getListeModules();
        
        for (int i = 0; i < listeModule.size(); i++) {
            // la moyenne obtenue pour le ième module
            double note = calculerMoyenneModule(listeModule.get(i));
            // le coefficient du ième module
            double coef = listeModule.get(i).getCoef();
            // on fait la somme des moyennes des modules de l'étudiant courant pour cette UE
            moyenne += note * coef;
            sommeCoeff += coef;
        }
        
        if (sommeCoeff > 0) {
            // on divise par la somme des coefficients des modules
            moyenne = moyenne/sommeCoeff;
        }
        
        return moyenne;
    }
    
    /**
     * Calcule la moyenne totale du semestre de l'étudiant
     * @return la moyenne totale de l'étudiant
     */
    public double calculerMoyenneTotale() {
        
        double moyenne = 0.0;
        double sommeCoef = 0.0;
        
        // On récupère la liste des UE de l'étudiant
        ArrayList<UniteEnseignement> listeUe = this.getPromo().getSemestre().getListeUE();
        
        for (int i = 0; i < listeUe.size(); i++) {
            // la moyenne obtenue pour la ième UE
            double note = calculerMoyenneUe(listeUe.get(i));
            // le coefficient de la ième UE
            double coef = listeUe.get(i).getCoefTotal();
            // on fait la somme des moyennes des UE pour cet étudiant
            moyenne += note * coef;
            sommeCoef += coef;
        }
        
        if (sommeCoef > 0) {
            // on divise par la somme des coefficients des UE
            moyenne = moyenne/sommeCoef;
        }
        
        return moyenne;
    }
    
    /**
     * Création du tableau de notes à insérer dans le document pdf
     * @return le tableau de notes de l'étudiant
     */
    public PdfPTable tableauNotes() {
        
        // Création du tableau de 3 colonnes
        PdfPTable table = new PdfPTable(3);
        
        // Objet de type cellule
        PdfPCell cell;
        
        // Cellule de total
        PdfPCell cellTotal;
        
        // Liste des UE suivies par l'étudiant
        ArrayList<UniteEnseignement> listeUE = promo.getSemestre().getListeUE();
        
        // On ajoute les lignes des UE
        for (int i = 0; i < listeUE.size(); i++) {
            UniteEnseignement ueCourante = listeUE.get(i);
            cell = new PdfPCell(new Phrase(ueCourante.getNom()));
            
            // Liste des modules associés à l'UE courante
            ArrayList<Module> listeModules = listeUE.get(i).getListeModules();
            
            // La cellule fusionne autant de lignes qu'il y a de modules
            cell.setRowspan(listeModules.size());
            // On ajoute la cellule au tableau
            table.addCell(cell);
            
            // Parcours de la liste des modules
            for (int j = 0; j < listeModules.size(); j++) {
                Module moduleCourant = listeModules.get(j);
                table.addCell(moduleCourant.getLibelle());
                table.addCell(String.valueOf(this.calculerMoyenneModule(moduleCourant)));
            }
            
            // Ligne de total de l'UE
            cellTotal = new PdfPCell(new Phrase("Total " + ueCourante.getCode()));
            cellTotal.setColspan(2);
            table.addCell(cellTotal);
            table.addCell(String.valueOf(this.calculerMoyenneUe(ueCourante)));
        }
        
        // On ajoute une ligne pour la moyenne totale
        cellTotal = new PdfPCell(new Phrase("Total du semestre"));
        cellTotal.setColspan(2);
        table.addCell(cellTotal);
        table.addCell(String.valueOf(this.calculerMoyenneTotale()));
        
        return table;
    }

    /**
     * Génère un fichier PDF contenant le relevé de notes de l'étudiant courant
     * @throws DocumentException 
     */
    public void genererReleve() throws DocumentException {
        
        // Le document pdf qui sera créé
        Document doc = new Document();
        
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(nom + " " + prenom + ".pdf"));
            doc.open();
            
            // Ajout de l'intitulé du semestre
            doc.add(new Paragraph("\t" + promo.getSemestre().getNom() + "\n\n"));
            
            // Ajout de le nom et le prénom de l'étudiat
            doc.add(new Paragraph("\t" + prenom + " " + nom + "\n\n"));
            doc.add(this.tableauNotes());
        } catch (DocumentException de) {
            de.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }    
        
        doc.close();
    }      
}