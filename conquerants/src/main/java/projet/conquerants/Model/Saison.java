package projet.conquerants.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Saison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String debut;
    private String fin;
    @OneToMany(mappedBy = "saison")
    private List<Equipe> equipes = new ArrayList<>();
    @OneToMany(mappedBy = "saison")
    private List<Joueur> joueurs = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getDebut() {
        return debut;
    }

    public String getFin() {
        return fin;
    }
}
