package projet.conquerants.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import projet.conquerants.Serializer.JeuSerializer;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private int division;
    @ManyToOne
    @JoinColumn(name = "id_jeu")
    @JsonSerialize(using = JeuSerializer.class)
    private Jeu jeu;
    @ManyToOne
    @JoinColumn(name = "id_saison")
    @JsonIgnoreProperties({"id"})
    private Saison saison;
    @OneToMany(mappedBy = "equipe")
    private List<Joueur> joueurs = new ArrayList<>();
    @OneToMany(mappedBy = "equipe1")
    private List<Match> matchs1 = new ArrayList<>();
    @OneToMany(mappedBy = "equipe2")
    private List<Match> matchs2 = new ArrayList<>();

    public Equipe() {

    }

    public Equipe(String nom, int division, Jeu jeu, Saison saison) {
        this.nom = nom;
        this.division = division;
        this.jeu = jeu;
        this.saison = saison;
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

    public Jeu getJeu() {
        return jeu;
    }

    public Saison getSaison() {
        return saison;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", division=" + division +
                ", jeu=" + jeu +
                ", saison=" + saison +
                '}';
    }
}
