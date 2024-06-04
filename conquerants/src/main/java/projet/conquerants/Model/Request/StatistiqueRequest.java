package projet.conquerants.Model.Request;

import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;

public class StatistiqueRequest {

    private String donnee;
    private String pseudo;
    private String jeu;
    private String saison;
    private int id_partie;

    public String getDonnee() {
        return donnee;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getJeu() {
        return jeu;
    }

    public String getSaison() {
        return saison;
    }

    public int getId_partie() {
        return id_partie;
    }
}
