package projet.conquerants.Model;

import jakarta.persistence.*;

@Entity
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean resultat;
    private boolean vote;
    @ManyToOne
    @JoinColumn(name = "id_match")
    private Match match;
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
