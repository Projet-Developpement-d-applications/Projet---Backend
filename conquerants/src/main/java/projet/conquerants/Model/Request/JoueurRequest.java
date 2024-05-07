package projet.conquerants.Model.Request;

import java.util.Date;

public class JoueurRequest {

    private String prenom;
    private String nom;
    private String pseudo;
    private Date date_naissance;
    private String jeu;
    private String saison;
    private String position;
    private String equipe;

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public String getJeu() {
        return jeu;
    }

    public String getSaison() {
        return saison;
    }

    public String getPosition() {
        return position;
    }

    public String getEquipe() {
        return equipe;
    }
}
