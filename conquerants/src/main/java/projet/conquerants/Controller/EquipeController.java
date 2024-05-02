package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;
import projet.conquerants.Request.EquipeRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EquipeController {

    private DatabaseService database;

    @Autowired
    public EquipeController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("equipeParJeu")
    public List<Equipe> equipeParJeu(@RequestBody EquipeRequest request) {
        List<Equipe> retour = new ArrayList<>();

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        List<Equipe> equipes = database.getEquipeParJeu(jeu, saison);
        if (!equipes.isEmpty()) {
            retour = equipes;
        }

        return retour;
    }

    @PostMapping("equipeParNom")
    public Equipe equipeParNom(@RequestBody EquipeRequest request) {
        Equipe retour = null;

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        Equipe equipe = database.getEquipeParNom(request.getNom(), jeu, saison);
        if (equipe != null) {
            retour = equipe;
        }

        return retour;
    }

    @PostMapping("creerEquipe")
    public String creerEquipe(@RequestBody EquipeRequest request) {
        String retour = "non";

        Equipe result = database.createEquipe(creerEquipeTemp(request));
        if (result != null) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("modifierEquipe")
    public String modifierEquipe(@RequestBody EquipeRequest request) {
        String retour = "non";

        Equipe result = database.modifyEquipe(modifierEquipeTemp(request));
        if (result != null) {
            retour = "oui";
        }

        return retour;
    }


    /** ajoute validation*/
    private Equipe creerEquipeTemp(EquipeRequest request) {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        return new Equipe(request.getNom(), request.getDivision(), jeu, saison);
    }

    private Equipe modifierEquipeTemp(EquipeRequest request) {
        Equipe equipe = database.getEquipeParId(request.getId());

        equipe.setNom(request.getNom());
        equipe.setDivision(request.getDivision());
        return equipe;
    }
}
