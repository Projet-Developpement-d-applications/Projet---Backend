package projet.conquerants.Model.Request;

import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;

public class StatistiqueRequest {

    private String donnee;
    private String pseudo;
    private Jeu jeu;
    private Saison saison;
    private int id_partie;

    public String getDonnee() {
        return donnee;
    }

    public String getPseudo() {
        return pseudo;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Saison getSaison() {
        return saison;
    }

    public int getId_partie() {
        return id_partie;
    }
}
