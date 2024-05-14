package projet.conquerants.Controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Request.MatchRequest;
import projet.conquerants.Service.DatabaseService;

import javax.crypto.Mac;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RestController
public class MatchController {

    private DatabaseService database;

    @Autowired
    public MatchController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("matchParEquipe")
    public List<Match> matchParEquipe(@RequestBody MatchRequest request) {
        Equipe equipe = database.getEquipeParId(request.getId_equipe());

        List<Match> matchs = database.getMatchsParEquipe(equipe).stream().filter(match -> match.getDate_match().getTime() < new Date().getTime()).toList();

        return matchs;
    }

    @PostMapping("matchParId")
    public Match matchParId(@RequestBody MatchRequest request) {
        return database.getMatchParId(request.getId_match());
    }

    @GetMapping("matchDeLaSemaine")
    public List<Match> matchDeLaSemaine() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date debut = calendar.getTime();
        Date fin = new Date();

        return database.getMatchDeLaSemaine(debut, fin);
    }

    @PostMapping("/admin/nouveauMatch")
    public ResponseEntity<String> nouveauMatch(@RequestBody MatchRequest request) {
        ResponseEntity<String> response = null;

        try {
            Equipe equipe1 = database.getEquipeParId(request.getId_equipe1());
            Equipe equipe2 = database.getEquipeParId(request.getId_equipe2());

            valideRequest(request, equipe1, equipe2);

            Match match = new Match(request.getScore1(), request.getScore2(), request.getDate(),
                    equipe1, equipe2);

            if (database.createMatch(match) != null) {
                response = ResponseEntity.ok("Le match a bien été créé");
            } else {
                response = ResponseEntity.status(403).body("Le match n'a pas pu être créé");
            }
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    @PutMapping("/admin/modifierMatch")
    public ResponseEntity<String> modifierMatch(@RequestBody MatchRequest request) {
        ResponseEntity<String> response = null;

        try {
            Equipe equipe1 = database.getEquipeParId(request.getId_equipe1());
            Equipe equipe2 = database.getEquipeParId(request.getId_equipe2());
            Match match = database.getMatchParId(request.getId_match());

            valideRequest(request, equipe1, equipe2);

            if (match == null) {
                throw new ExistePasException();
            }

            match.setEquipe1(equipe1);
            match.setEquipe2(equipe2);
            match.setDate_match(request.getDate());
            match.setScore1(request.getScore1());
            match.setScore2(request.getScore2());
            match.setJouer(request.getJouer());

            match = database.modifierMatch(match);

            if (match != null) {
                if (match.getJouer()) {
                    setPredictionVote(match);
                }

                response = ResponseEntity.ok("Le match a bien été modifié");

            } else {
                response = ResponseEntity.status(403).body("Le match n'a pas pu être modifié");
            }
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        } catch (ExistePasException e) {
            response = ResponseEntity.status(403).body("Le match que vous recherchez n'existe pas");
        }

        return response;
    }

    @PostMapping("/admin/supprimerMatch")
    public ResponseEntity<String> supprimerMatch(@RequestBody MatchRequest request) {
        Match match = database.getMatchParId(request.getId_match());
        List<Partie> parties = database.getPartiesParMatch(match);

        database.deleteParties(parties);
        database.deleteMatch(match);

        return ResponseEntity.ok("Match supprimé");
    }

    private void setPredictionVote(Match match) {
        List<Prediction> predictions = database.getPredictionParMatch(match);
        for (Prediction p : predictions) {
            if (match.getScore1() > match.getScore2()) {
                p.setResultat(p.isVote());
            } else {
                p.setResultat(!p.isVote());
            }

            database.modifierPrediction(p);
        }
    }

    private void valideRequest(MatchRequest request, Equipe equipe1, Equipe equipe2) throws ManqueInfoException {
        if (request.getScore1() < 0 || request.getScore2() < 0 || request.getDate() == null ||
                equipe1 == null || equipe2 == null) {
            throw new ManqueInfoException();
        }
    }
}