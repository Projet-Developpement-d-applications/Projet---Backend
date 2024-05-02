package projet.conquerants.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Partie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score1;
    private int score2;
    @ManyToOne
    @JoinColumn(name = "id_match")
    private Match match;
    @OneToMany(mappedBy = "partie")
    private List<Statistique> statistiques = new ArrayList<>();
}
