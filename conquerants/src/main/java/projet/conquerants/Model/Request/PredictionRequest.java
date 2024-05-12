package projet.conquerants.Model.Request;

public class PredictionRequest {

    private int id_prediction;
    private boolean vote;
    private int id_match;
    private int id_utilisateur;

    public int getId_prediction() {
        return id_prediction;
    }

    public boolean isVote() {
        return vote;
    }

    public int getId_match() {
        return id_match;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }
}
