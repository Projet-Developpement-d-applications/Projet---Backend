package projet.conquerants.Model.Request;

import java.util.Date;

public class MatchRequest {

    private int id_match;

    // pour obtenir une seule equipe
    private int id_equipe;

    // pour la création d'un match
    private int id_equipe1;
    private int id_equipe2;
    private int score1;
    private int score2;
    private String date;
    private boolean jouer;

    public int getId_match() {
        return id_match;
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public String getDate() {
        return date;
    }

    public int getId_equipe1() {
        return id_equipe1;
    }

    public int getId_equipe2() {
        return id_equipe2;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public boolean getJouer() {
        return jouer;
    }
}
