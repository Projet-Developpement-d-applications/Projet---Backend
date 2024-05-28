package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Request.MatchRequest;
import projet.conquerants.Service.DatabaseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class MatchController {

    private DatabaseService database;

    @Autowired
    public MatchController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("matchParEquipe")
    public List<Match> matchParEquipe(@RequestBody MatchRequest request) {
        Equipe equipe = database.getEquipeParId(request.getId_equipe());

        List<Match> matchs = database.getMatchsParEquipe(equipe);

        return matchs;
    }

    @PostMapping("matchParId")
    public Match matchParId(@RequestBody MatchRequest request) {
        return database.getMatchParId(request.getId_match());
    }

    @GetMapping("/noAuth/matchs")
    public List<Match> matchs() {
        List<Match> matchs = new ArrayList<>();

        matchs = database.getMatchs();

        return matchs;
    }

    @GetMapping("/noAuth/matchDeLaSemaine")
    public List<Match> matchDeLaSemaine() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date debut = calendar.getTime();
        Date fin = new Date();

        return database.getMatchDeLaSemaine(debut, fin);
    }

    @GetMapping("/noAuth/matchAVenir")
    public List<Match> matchAVenir() {
        Date now = new Date();

        return database.getMatchAVenir(now);
    }

    @PostMapping("/admin/nouveauMatch")
    public ResponseEntity<String> nouveauMatch(@RequestBody MatchRequest request) {
        ResponseEntity<String> response = null;

        try {
            Equipe equipe1 = database.getEquipeParId(request.getId_equipe1());
            Equipe equipe2 = database.getEquipeParId(request.getId_equipe2());

            valideRequest(request, equipe1, equipe2);
            valideExisteDeja(request, equipe1, equipe2);

            Date date = formatDate(request.getDate());

            Match match = new Match(request.getScore1(), request.getScore2(), date,
                    equipe1, equipe2);

            if (database.createMatch(match) != null) {
                response = ResponseEntity.ok("Le match a bien été créé");
            } else {
                response = ResponseEntity.status(403).body("Le match n'a pas pu être créé");
            }
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Un match existe déjà à cette date");
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

            Date date = formatDate(request.getDate());

            match.setEquipe1(equipe1);
            match.setEquipe2(equipe2);
            match.setDate_match(date);
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
        Date date = formatDate(request.getDate());

        if (request.getScore1() < 0 || request.getScore2() < 0 || date == null ||
                equipe1 == null || equipe2 == null) {
            throw new ManqueInfoException();
        }
    }

    private void valideExisteDeja(MatchRequest request, Equipe equipe1, Equipe equipe2) throws ExisteDejaException {
        List<Match> matches = database.getMatchsParEquipe(equipe1).stream().filter(match -> match.getEquipe2().getId() == equipe2.getId()).toList();

        if (matches.stream().anyMatch(match -> Objects.equals(match.getDate_match(), formatDate(request.getDate())))) {
            throw new ExisteDejaException();
        }
    }

    private Date formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date.replace(" ", "T"), formatter);

        // Convert LocalDateTime to Date
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}
