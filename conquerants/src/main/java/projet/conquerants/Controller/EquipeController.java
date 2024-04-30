package projet.conquerants.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Equipe;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EquipeController {

    private DatabaseController database;

    public EquipeController() {
        database = new DatabaseController();
    }

    @PostMapping("equipeParJeu")
    public List<Equipe> equipeParJeu(@RequestBody Equipe teamRequest) {
        List<Equipe> retour = new ArrayList<>();

        List<Equipe> equipes = database.getTeamByGame(teamRequest);
        if (!equipes.isEmpty()) {
            retour = equipes;
        }

        return retour;
    }

    @PostMapping("equipeParNom")
    public Equipe equipeParNom(@RequestBody Equipe teamRequest) {
        Equipe retour = null;

        Equipe equipe = database.getTeamByName(teamRequest);
        if (equipe != null) {
            retour = equipe;
        }

        return retour;
    }

    @PostMapping("creerEquipe")
    public String creerEquipe(@RequestBody Equipe teamRequest) {
        String retour = "non";

        boolean isCreated = database.createTeam(teamRequest);
        if (isCreated) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("modifierEquipe")
    public String modifierEquipe(@RequestBody Equipe teamRequest) {
        String retour = "non";

        boolean isModified = database.modifyTeam(teamRequest);
        if (isModified) {
            retour = "oui";
        }

        return retour;
    }
}
