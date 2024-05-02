package projet.conquerants.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "positions")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    @ManyToOne
    @JoinColumn(name = "id_jeu")
    private Jeu jeu;
    @OneToMany(mappedBy = "position")
    private List<Joueur> joueurs = new ArrayList<>();

    public String getNom() {
        return this.nom;
    }
}
