package projet.conquerants.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Joueur;

import java.util.ArrayList;
import java.util.List;

@RestController
public class JoueurController {

    private DatabaseController database;

    public JoueurController() {
        database = new DatabaseController();
    }

    @PostMapping("joueurParPseudo")
    public Joueur joueurParPseudo(@RequestBody Joueur playerRequest) {
        Joueur retour = null;

        Joueur joueur = database.getPlayerByName(playerRequest);
        if (joueur != null) {
            retour = joueur;
        }

        return retour;
    }

    @PostMapping("joueurParEquipe")
    public List<Joueur> joueurParEquipe(@RequestBody Joueur playerRequest) {
        List<Joueur> retour = new ArrayList<>();

        List<Joueur> joueurs = database.getPlayerByTeam(playerRequest);
        if (!joueurs.isEmpty()) {
            retour = joueurs;
        }

        return retour;
    }

    @PostMapping("creerJoueur")
    public String creerJoueur(@RequestBody Joueur playerRequest) {
        String retour = "non";

        boolean isCreated = database.createPlayer(playerRequest);
        if (isCreated) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("creerJoueurs")
    public String creerJoueurs(@RequestBody List<Joueur> playerRequest) {
        String retour = "non";

        int rowCreated = database.createPlayers(playerRequest);
        if (rowCreated > 0) {
            retour = "added " + rowCreated;
        }

        return retour;
    }

    @PostMapping("modifierJoueur")
    public String modifierJoueur(@RequestBody Joueur playerRequest) {
        String retour = "non";

        boolean isModifed = database.modifyPlayer(playerRequest);
        if (isModifed) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("modifierJoueurs")
    public String modifierJoueurs(@RequestBody List<Joueur> playerRequest) {
        String retour = "non";

        int rowModified = database.modifyPlayers(playerRequest);
        if (rowModified > 0) {
            retour = "modified " + rowModified;
        }

        return retour;
    }


}
