package projet.conquerants.Model;

public class Equipe {

    private int id;
    private String nom;
    private int division;
    private int id_jeu;
    private int id_saison;

    public Equipe() {
    }

    public Equipe(int id, String nom, int division, int id_jeu, int id_saison) {
        this.id = id;
        this.nom = nom;
        this.division = division;
        this.id_jeu = id_jeu;
        this.id_saison = id_saison;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getDivision() {
        return division;
    }

    public int getId_jeu() {
        return id_jeu;
    }

    public int getId_saison() {
        return id_saison;
    }
}
