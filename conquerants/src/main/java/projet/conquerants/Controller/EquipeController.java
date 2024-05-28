package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Request.EquipeRequest;
import projet.conquerants.Model.Saison;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class EquipeController {

    private DatabaseService database;
    private ValidationService validation;

    @Autowired
    public EquipeController(DatabaseService database, ValidationService validation) {
        this.database = database;
        this.validation = validation;
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

    @GetMapping("/noAuth/toutesEquipesLimoilou")
    public List<Equipe> equipesLimoilou() {
        List<Equipe> equipes = new ArrayList<>();

        equipes = database.getEquipes().stream()
                .filter(equipe -> equipe.getNom().contains("Conquérants"))
                .map(equipe -> new Equipe(equipe.getId(), equipe.getNom(), equipe.getDivision(), equipe.getJeu(), equipe.getSaison()))
                .toList();

        return equipes;
    }

    @PostMapping("/noAuth/equipeLimoilouParJeu")
    public List<Equipe> equipeLimoilouParJeu(@RequestBody EquipeRequest request) {
        List<Equipe> retour = new ArrayList<>();

        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        List<Equipe> equipes = database.getEquipeParJeu(jeu, saison);
        if (!equipes.isEmpty()) {
            retour = equipes.stream().filter(equipe -> equipe.getNom().contains("Conquérants")).toList();
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

    @PostMapping("/admin/creerEquipe")
    public ResponseEntity<String> creerEquipe(@RequestBody EquipeRequest request) {
        ResponseEntity<String> response = null;

        try {
            Equipe result = database.createEquipe(creerEquipeTemp(request));
            if (result != null) {
                response = ResponseEntity.ok("Équipe créé avec succès");
            } else {
                response = ResponseEntity.status(403).body("L'équipe n'a pas pu être créé");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Une équipe avec ce nom existe déjà pour ce jeu et cette saison");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    @PutMapping("/admin/modifierEquipe")
    public ResponseEntity<String> modifierEquipe(@RequestBody EquipeRequest request) {
        ResponseEntity<String> response = null;

        try {
            Equipe result = database.modifyEquipe(modifierEquipeTemp(request));
            if (result != null) {
                response = ResponseEntity.ok("Équipe modifié avec succès");
            } else {
                response = ResponseEntity.status(403).body("L'équipe n'a pas pu être modifié");
            }
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Une équipe avec ce nom existe déjà pour ce jeu et cette saison");
        } catch (ExistePasException e) {
            response = ResponseEntity.status(403).body("Aucune équipe correspondant à cet id");
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    private Equipe creerEquipeTemp(EquipeRequest request) throws RuntimeException {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());

        valideCreerRequest(request, jeu, saison);
        valideEquipeExistePas(request, jeu, saison);

        return new Equipe(request.getNom(), request.getDivision(), jeu, saison);
    }

    private Equipe modifierEquipeTemp(EquipeRequest request) throws RuntimeException {
        Equipe equipe = database.getEquipeParId(request.getId());

        if (equipe == null) {
            throw new ExistePasException();
        }

        valideModifierRequest(request);
        valideEquipeExistePas(request, equipe.getJeu(), equipe.getSaison());

        equipe.setNom(request.getNom());
        equipe.setDivision(request.getDivision());
        return equipe;
    }

    private void valideEquipeExistePas(EquipeRequest request, Jeu jeu, Saison saison) throws ExisteDejaException {
        Equipe equipeTemp = database.getEquipeParNom(request.getNom(), jeu, saison);
        if (equipeTemp != null && equipeTemp.getId() != request.getId()) {
            throw new ExisteDejaException();
        }
    }

    private void valideCreerRequest(EquipeRequest request, Jeu jeu, Saison saison) throws ManqueInfoException {
        if (!validation.valideStringOfCharAndDigitsWithSpace(request.getNom()) || !validation.valideStringOfCharAndDigitsWithSpace(request.getJeu()) ||
                !validation.valideStringOfCharAndDigits(request.getSaison()) || jeu == null
                || saison == null) {
            throw new ManqueInfoException();
        }
    }

    private void valideModifierRequest(EquipeRequest request) throws ManqueInfoException {
        if (!validation.valideStringOfCharAndDigitsWithSpace(request.getNom())) {
            throw new ManqueInfoException();
        }
    }
}
