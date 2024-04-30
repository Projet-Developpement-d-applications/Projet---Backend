package projet.conquerants.Model;

public class Utilisateur {
    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String mot_passe;
    private int role_id;

    public Utilisateur() {

    }

    public Utilisateur(int id, String prenom, String nom, String pseudo, String mot_passe, int role_id) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.mot_passe = mot_passe;
        this.role_id = role_id;
    }

    public int getId() {
        return id;
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

    public int getRole() {
        return role_id;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", mot_passe='" + mot_passe + '\'' +
                ", role=" + role_id +
                '}';
    }
}
