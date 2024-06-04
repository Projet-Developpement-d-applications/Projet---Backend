package projet.conquerants.Model;

import jakarta.persistence.*;

@Entity
public class Statistique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String donnee;
    @ManyToOne
    @JoinColumn(name = "id_joueur")
    private Joueur joueur;
    @ManyToOne
    @JoinColumn(name = "id_partie")
    private Partie partie;

    public Statistique() {

    }

    public Statistique(String donnee, Joueur joueur, Partie partie) {
        this.donnee = donnee;
        this.joueur = joueur;
        this.partie = partie;
    }
}
