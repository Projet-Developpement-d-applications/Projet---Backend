package projet.conquerants.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import projet.conquerants.Serializer.MatchSerializer;
import projet.conquerants.Serializer.UtilisateurSerializer;

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
    @JsonSerialize(using = UtilisateurSerializer.class)
    private Utilisateur utilisateur;

    public Prediction() {

    }

    public Prediction(boolean vote, Match match, Utilisateur utilisateur) {
        this.vote = vote;
        this.match = match;
        this.utilisateur = utilisateur;
    }

    public int getId() {
        return id;
    }

    public boolean isResultat() {
        return resultat;
    }

    public boolean isVote() {
        return vote;
    }

    public Match getMatch() {
        return match;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setResultat(boolean resultat) {
        this.resultat = resultat;
    }
}
