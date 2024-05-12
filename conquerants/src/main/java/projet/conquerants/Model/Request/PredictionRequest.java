package projet.conquerants.Model.Request;

public class PredictionRequest {

    private int id_prediction;
    private boolean vote;
    private int id_match;
    private String pseudo;

    public int getId_prediction() {
        return id_prediction;
    }

    public boolean getVote() {
        return vote;
    }

    public int getId_match() {
        return id_match;
    }

    public String getPseudo() {
        return pseudo;
    }
}
