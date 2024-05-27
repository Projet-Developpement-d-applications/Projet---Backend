package projet.conquerants.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import projet.conquerants.Serializer.JeuSerializer;
import projet.conquerants.Serializer.PositionSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Joueur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    private Date date_naissance;
    @ManyToOne
    @JoinColumn(name = "id_position")
    @JsonSerialize(using = PositionSerializer.class)
    private Position position;
    @ManyToOne
    @JoinColumn(name = "id_equipe")
    @JsonIgnoreProperties({"jeu", "saison", "joueurs"})
    private Equipe equipe;
    @ManyToOne
    @JoinColumn(name = "id_jeu")
    @JsonSerialize(using = JeuSerializer.class)
    private Jeu jeu;
    @ManyToOne
    @JoinColumn(name = "id_saison")
    private Saison saison;
    @OneToMany(mappedBy = "joueur")
    private List<Statistique> statistiques = new ArrayList<>();

    public Joueur() {

    }

    public Joueur(String prenom, String nom, String pseudo, Date date_naissance, Position position, Equipe equipe, Jeu jeu, Saison saison) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.date_naissance = date_naissance;
        this.position = position;
        this.equipe = equipe;
        this.jeu = jeu;
        this.saison = saison;
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

    public Date getDate_naissance() {
        return date_naissance;
    }

    public Position getPosition() {
        return position;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Saison getSaison() {
        return saison;
    }

    public List<Statistique> getStatistiques() {
        return statistiques;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", date_naissance=" + date_naissance +
                ", position=" + position +
                ", equipe=" + equipe +
                ", jeu=" + jeu +
                ", saison=" + saison +
                '}';
    }
}
