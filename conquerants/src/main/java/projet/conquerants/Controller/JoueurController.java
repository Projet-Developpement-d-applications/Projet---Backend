package projet.conquerants.Controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.*;
import projet.conquerants.Model.Request.JoueurRequest;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class JoueurController {

    private DatabaseService database;
    private ValidationService validation;

    @Autowired
    public JoueurController(DatabaseService database, ValidationService validation) {
        this.database = database;
        this.validation = validation;
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
    public ResponseEntity<String> creerJoueur(@RequestBody JoueurRequest request) {
        ResponseEntity<String> response = null;

        try {
            Joueur joueur = database.createJoueur(creerJoueurTemp(request));
            if (joueur != null) {
                response = ResponseEntity.ok("Le joueur a bien été créé");
            } else {
                ResponseEntity.status(403).body("Le joueur n'a pas pu être créé");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Un joueur avec ce pseudo existe déjà pour ce jeu et cette saison");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    @PostMapping("/admin/creerJoueurs")
    public ResponseEntity<String> creerJoueurs(@RequestBody List<JoueurRequest> request) {
        ResponseEntity<String> response = null;
        try {
            int rowCreated = database.createJoueurs(creerJoueursTemp(request)).size();
            if (rowCreated > 0) {
                response = ResponseEntity.ok(rowCreated + "/" + request.size() + " joueurs créés");
            } else {
                response = ResponseEntity.status(403).body("Aucun joueur n'a pu être créé");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Un joueur avec ce pseudo existe déjà pour ce jeu et cette saison");
        } catch (ExistePasException e) {
            response = ResponseEntity.status(403).body("Le joueur que vous souhaitez modifié n'existe pas");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    @PutMapping("/admin/modifierJoueur")
    public ResponseEntity<String> modifierJoueur(@RequestBody JoueurRequest request) {
        ResponseEntity<String> response = null;

        try {
            Joueur joueur = database.modifyJoueur(modifierJoueurTemp(request));
            if (joueur != null) {
                response = ResponseEntity.ok("Le joueur a bien été modifié");
            } else {
                ResponseEntity.status(403).body("Le joueur n'a pas pu être modifié");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Un joueur avec ce pseudo existe déjà pour ce jeu et cette saison");
        } catch (ExistePasException e) {
            response = ResponseEntity.status(403).body("Le joueur que vous souhaitez modifié n'existe pas");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }


        return response;
    }

    @PutMapping("/admin/modifierJoueurs")
    public ResponseEntity<String> modifierJoueurs(@RequestBody List<JoueurRequest> request) {
        ResponseEntity<String> response = null;

        try {
            int rowModified = database.modifyJoueurs(modifierJoueursTemp(request)).size();
            if (rowModified > 0) {
                response = ResponseEntity.ok(rowModified + "/" + request.size() + " joueurs modifiés");
            } else {
                response = ResponseEntity.status(403).body("Aucun joueur n'a pu être créé");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Un joueur avec ce pseudo existe déjà pour ce jeu et cette saison");
        } catch (ExistePasException e) {
            response = ResponseEntity.status(403).body("Le joueur que vous souhaitez modifié n'existe pas");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    private Joueur creerJoueurTemp(JoueurRequest request) throws RuntimeException {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Equipe equipe = database.getEquipeParNom(request.getEquipe(), jeu, saison);
        Position position = database.getPositionParNom(request.getPosition());

        valideRequest(request, jeu, saison, equipe);
        valideExisteDeja(request.getPseudo(), jeu, saison);

        return new Joueur(request.getPrenom(), request.getNom(), request.getPseudo(),
                request.getDate_naissance(), position, equipe, jeu, saison);
    }

    private List<Joueur> creerJoueursTemp(List<JoueurRequest> request) throws RuntimeException {
        List<Joueur> joueursTemp = new ArrayList<>();
        for (JoueurRequest jr : request) {
            joueursTemp.add(creerJoueurTemp(jr));
        }

        return joueursTemp;
    }

    private Joueur modifierJoueurTemp(JoueurRequest request) throws RuntimeException {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Joueur joueurTemp = database.getJoueurParId(request.getId()).orElseThrow();
        Position position = database.getPositionParNom(request.getPosition());
        Equipe equipe = database.getEquipeParNom(request.getEquipe(), jeu, saison);

        valideRequest(request, jeu, saison, equipe);
        if (!Objects.equals(joueurTemp.getPseudo(), request.getPseudo())) {
            valideExisteDeja(request.getPseudo(), jeu, saison);
        }

        joueurTemp.setPrenom(request.getPrenom());
        joueurTemp.setNom(request.getNom());
        joueurTemp.setPseudo(request.getPseudo());
        joueurTemp.setDate_naissance(request.getDate_naissance());
        joueurTemp.setEquipe(equipe);
        joueurTemp.setPosition(position);

        return joueurTemp;
    }

    private void valideExisteDeja(String pseudo, Jeu jeu, Saison saison) throws ExisteDejaException {
        if (database.getJoueurParNom(pseudo, jeu, saison) != null) {
            throw new ExisteDejaException();
        }
    }

    private List<Joueur> modifierJoueursTemp(List<JoueurRequest> request) throws RuntimeException {
        List<Joueur> joueursTemp = new ArrayList<>();
        for (JoueurRequest jr : request) {
            joueursTemp.add(modifierJoueurTemp(jr));
        }

        return joueursTemp;
    }

    private void valideRequest(JoueurRequest request, Jeu jeu, Saison saison, Equipe equipe) throws ManqueInfoException {
        if (!validation.valideStringOfNomPrenom(request.getPrenom()) || !validation.valideStringOfNomPrenom(request.getNom()) ||
                !validation.valideStringOfCharAndDigitsWithSpace(request.getPseudo()) || request.getDate_naissance() == null ||
                jeu == null || saison == null || equipe == null) {
            throw new ManqueInfoException();
        }
    }


}
