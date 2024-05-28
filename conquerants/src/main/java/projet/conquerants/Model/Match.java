package projet.conquerants.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "date_match")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "id_equipe1")
    @JsonIgnoreProperties("joueurs")
    private Equipe equipe1;
    @ManyToOne
    @JoinColumn(name = "id_equipe2")
    @JsonIgnoreProperties("joueurs")
    private Equipe equipe2;
    private boolean jouer;
    @OneToMany(mappedBy = "match")
    private List<Partie> parties = new ArrayList<>();
    @OneToMany(mappedBy = "match")
    private List<Prediction> predictions = new ArrayList<>();

    public Match() {
    }

    public Match(int score1, int score2, Date date_match, Equipe equipe1, Equipe equipe2) {
        this.score1 = score1;
        this.score2 = score2;
        this.date = date_match;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.jouer = false;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setDate_match(Date date_match) {
        this.date = date_match;
    }

    public void setEquipe1(Equipe equipe1) {
        this.equipe1 = equipe1;
    }

    public void setEquipe2(Equipe equipe2) {
        this.equipe2 = equipe2;
    }

    public Equipe getEquipe1() {
        return equipe1;
    }

    public Equipe getEquipe2() {
        return equipe2;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public Date getDate_match() {
        return date;
    }

    public int getId() {
        return id;
    }

    public boolean getJouer() {
        return jouer;
    }

    public void setJouer(boolean jouer) {
        this.jouer = jouer;
    }
}
