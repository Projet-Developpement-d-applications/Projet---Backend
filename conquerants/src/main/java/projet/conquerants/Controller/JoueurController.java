package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.*;
import projet.conquerants.Model.Request.JoueurRequest;
import projet.conquerants.Service.DatabaseService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class JoueurController {

    private DatabaseService database;

    @Autowired
    public JoueurController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("joueurParPseudo")
    public Joueur joueurParPseudo(@RequestBody JoueurRequest request) {
        Joueur retour = null;

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        Joueur joueur = database.getJoueurParNom(request.getPseudo(), jeu, saison);
        if (joueur != null) {
            retour = joueur;
        }

        return retour;
    }

    @PostMapping("joueurParEquipe")
    public List<Joueur> joueurParEquipe(@RequestBody JoueurRequest request) {
        List<Joueur> retour = new ArrayList<>();

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Equipe equipe = database.getEquipeParNom(request.getEquipe(), jeu, saison);

        List<Joueur> joueurs = database.getJoueursParEquipe(equipe);
        if (!joueurs.isEmpty()) {
            retour = joueurs;
        }

        return retour;
    }

    @PostMapping("/admin/creerJoueur")
    public String creerJoueur(@RequestBody JoueurRequest request) {
        String retour = "non";

        Joueur joueur = database.createJoueur(creerJoueurTemp(request));
        if (joueur != null) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("/admin/creerJoueurs")
    public String creerJoueurs(@RequestBody List<JoueurRequest> request) {
        String retour = "non";

        int rowCreated = database.createJoueurs(creerJoueursTemp(request)).size();
        if (rowCreated > 0) {
            retour = "added " + rowCreated;
        }

        return retour;
    }

    @PostMapping("/admin/modifierJoueur")
    public String modifierJoueur(@RequestBody JoueurRequest request) {
        String retour = "non";

        Joueur joueur = database.modifyJoueur(modifierJoueurTemp(request));
        if (joueur != null) {
            retour = "oui";
        }

        return retour;
    }

    @PostMapping("/admin/modifierJoueurs")
    public String modifierJoueurs(@RequestBody List<JoueurRequest> request) {
        String retour = "non";

        int rowModified = database.modifyJoueurs(modifierJoueursTemp(request)).size();
        if (rowModified > 0) {
            retour = "modified " + rowModified;
        }

        return retour;
    }

    private Joueur creerJoueurTemp(JoueurRequest request) {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Equipe equipe = database.getEquipeParNom(request.getEquipe(), jeu, saison);
        Position position = database.getPositionParNom(request.getPosition());

        Joueur joueurTemp = new Joueur(request.getPrenom(), request.getNom(), request.getPseudo(),
                request.getDate_naissance(), position, equipe, jeu, saison);

        return joueurTemp;
    }

    private List<Joueur> creerJoueursTemp(List<JoueurRequest> request) {
        List<Joueur> joueursTemp = new ArrayList<>();
        for (JoueurRequest jr: request) {
            joueursTemp.add(creerJoueurTemp(jr));
        }

        return joueursTemp;
    }

    private Joueur modifierJoueurTemp(JoueurRequest request) {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Joueur joueurTemp = database.getJoueurParNom(request.getPseudo(), jeu, saison);

        if (request.getPrenom() != null) {
            joueurTemp.setPrenom(request.getPrenom());
        }
        if (request.getNom() != null) {
            joueurTemp.setNom(request.getNom());
        }
        if (request.getPseudo() != null) {
            joueurTemp.setPseudo(request.getPseudo());
        }
        if (request.getPosition() != null) {
            Position position = database.getPositionParNom(request.getPosition());
            joueurTemp.setPosition(position);
        }
        if (request.getEquipe() != null) {
            Equipe equipe = database.getEquipeParNom(request.getEquipe(), jeu, saison);
            joueurTemp.setEquipe(equipe);
        }

        return database.modifyJoueur(joueurTemp);
    }

    private List<Joueur> modifierJoueursTemp(List<JoueurRequest> request) {
        List<Joueur> joueursTemp = new ArrayList<>();
        for (JoueurRequest jr: request) {
            joueursTemp.add(modifierJoueurTemp(jr));
        }

        return joueursTemp;
    }


}
