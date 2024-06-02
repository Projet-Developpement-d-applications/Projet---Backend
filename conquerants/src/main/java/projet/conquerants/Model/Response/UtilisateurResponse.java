package projet.conquerants.Model.Response;

public class UtilisateurResponse {

    private String nom;
    private String prenom;
    private String pseudo;
    private String role;

    public UtilisateurResponse(String nom, String prenom, String pseudo, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getRole() {
        return role;
    }
}
