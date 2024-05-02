package projet.conquerants.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "matchs")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score1;
    private int score2;
    private Date date_match;
    @ManyToOne
    @JoinColumn(name = "id_equipe1")
    private Equipe equipe1;
    @ManyToOne
    @JoinColumn(name = "id_equipe2")
    private Equipe equipe2;
    @OneToMany(mappedBy = "match")
    private List<Partie> parties = new ArrayList<>();
    @OneToMany(mappedBy = "match")
    private List<Prediction> predictions = new ArrayList<>();
}
