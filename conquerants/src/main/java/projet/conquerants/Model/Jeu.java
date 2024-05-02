package projet.conquerants.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Jeu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private Date date_sortie;
    @ManyToOne
    @JoinColumn(name = "id_genre")
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "id_developpeur")
    private Developpeur dev;
    @OneToMany(mappedBy = "jeu")
    private List<Equipe> equipes = new ArrayList<>();
    @OneToMany(mappedBy = "jeu")
    private List<Joueur> joueurs = new ArrayList<>();
    @OneToMany(mappedBy = "jeu")
    private List<Position> positions = new ArrayList<>();

    public Jeu() {

    }

    public Jeu(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Date getDate_sortie() {
        return date_sortie;
    }

    public Genre getGenre() {
        return genre;
    }

    public Developpeur getDev() {
        return dev;
    }
}
