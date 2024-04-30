package projet.conquerants.Model;

public class Joueur {
    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String date_naissance;
    private int id_position;
    private int id_equipe;
    private int id_jeu;
    private int id_saison;

    public Joueur() {

    }

    public Joueur(int id, String prenom, String nom, String pseudo, String date_naissance, int id_position, int id_equipe, int id_jeu, int id_saison) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.date_naissance = date_naissance;
        this.id_position = id_position;
        this.id_equipe = id_equipe;
        this.id_jeu = id_jeu;
        this.id_saison = id_saison;
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

    public String getDate_naissance() {
        return date_naissance;
    }

    public int getId_position() {
        return id_position;
    }

    public int getId_equipe() {
        return id_equipe;
    }

    public int getId_jeu() {
        return id_jeu;
    }

    public int getId_saison() {
        return id_saison;
    }
}
