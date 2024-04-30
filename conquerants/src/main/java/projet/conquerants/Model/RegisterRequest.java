package projet.conquerants.Model;

public class RegisterRequest {

    private String prenom;
    private String nom;
    private String pseudo;
    private String mot_passe;

    public RegisterRequest() {
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMot_passe() {
        return mot_passe;
    }
}
