package projet.conquerants.Model.Request;

import java.util.Date;

public class JoueurRequest {

    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String date_naissance;
    private String jeu;
    private String saison;
    private String position;
    private int equipe;

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getDate_naissance() {
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

    public int getEquipe() {
        return equipe;
    }

    public int getId() {
        return id;
    }
}
