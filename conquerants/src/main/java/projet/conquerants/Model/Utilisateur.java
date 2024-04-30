package projet.conquerants.Model;

public class Utilisateur {
    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String mot_passe;
    private Role role;

    public Utilisateur() {

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

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", mot_passe='" + mot_passe + '\'' +
                ", role=" + role +
                '}';
    }
}
