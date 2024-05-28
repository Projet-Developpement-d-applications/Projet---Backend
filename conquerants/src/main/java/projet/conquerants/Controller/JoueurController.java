package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.*;
import projet.conquerants.Model.Request.JoueurRequest;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.ValidationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    @GetMapping("/noAuth/getAllJoueurs")
    public List<Joueur> getJoueurs() {
        List<Joueur> retour = null;

        List<Joueur> joueur = database.getJoueurs();
        if (joueur != null) {
            retour = joueur;
        }

        return retour;
    }

    @PostMapping("/noAuth/joueursParJeu")
    public List<Joueur> joueursParJeu(@RequestBody JoueurRequest request) {
        List<Joueur> retour = null;

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        List<Joueur> joueur = database.getJoueursParJeu(jeu, saison);
        if (joueur != null) {
            retour = joueur;
        }

        return retour;
    }

    @PostMapping("/noAuth/joueurParPseudo")
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

        Equipe equipe = database.getEquipeParId(request.getEquipe());

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
        Equipe equipe = database.getEquipeParId(request.getEquipe());
        Position position = database.getPositionParNom(request.getPosition());

        valideRequest(request, jeu, saison, equipe);
        valideExisteDeja(request.getPseudo(), jeu, saison);

        Date date = formatDate(request.getDate_naissance());

        return new Joueur(request.getPrenom(), request.getNom(), request.getPseudo(),
                date, position, equipe, jeu, saison);
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
        Equipe equipe = database.getEquipeParId(request.getEquipe());

        valideRequest(request, jeu, saison, equipe);
        if (!Objects.equals(joueurTemp.getPseudo(), request.getPseudo())) {
            valideExisteDeja(request.getPseudo(), jeu, saison);
        }

        Date date = formatDate(request.getDate_naissance());

        joueurTemp.setPrenom(request.getPrenom());
        joueurTemp.setNom(request.getNom());
        joueurTemp.setPseudo(request.getPseudo());
        joueurTemp.setDate_naissance(date);
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
        Date date = formatDate(request.getDate_naissance());

        if (!validation.valideStringOfNomPrenom(request.getPrenom()) || !validation.valideStringOfNomPrenom(request.getNom()) ||
                !validation.valideStringOfCharAndDigitsWithSpace(request.getPseudo()) || date == null ||
                jeu == null || saison == null || equipe == null) {
            throw new ManqueInfoException();
        }
    }

    private Date formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Convert LocalDate to Date
        return java.sql.Date.valueOf(localDate);
    }
}
