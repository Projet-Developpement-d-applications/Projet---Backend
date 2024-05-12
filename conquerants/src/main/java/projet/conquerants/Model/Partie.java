package projet.conquerants.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import projet.conquerants.Serializer.MatchSerializer;

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
    @JsonSerialize(using = MatchSerializer.class)
    private Match match;
    @OneToMany(mappedBy = "partie")
    private List<Statistique> statistiques = new ArrayList<>();

    public Partie() {
    }

    public Partie(int score1, int score2, Match match) {
        this.score1 = score1;
        this.score2 = score2;
        this.match = match;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getId() {
        return id;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public Match getMatch() {
        return match;
    }
}
