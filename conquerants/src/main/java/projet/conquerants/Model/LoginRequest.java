package projet.conquerants.Model;

public class LoginRequest {

    private String pseudo;
    private String mot_passe;

    public LoginRequest() {

    }

    public LoginRequest(String pseudo, String mot_passe) {
        this.pseudo = pseudo;
        this.mot_passe = mot_passe;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMot_passe() {
        return mot_passe;
    }
}
