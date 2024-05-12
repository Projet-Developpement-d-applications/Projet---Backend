package projet.conquerants.Controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;
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

        Equipe equipe1 = database.getEquipeParId(request.getId_equipe1());
        Equipe equipe2 = database.getEquipeParId(request.getId_equipe2());

        Match match = new Match(request.getScore1(), request.getScore2(), request.getDate(),
                equipe1, equipe2);

        if (database.createMatch(match) != null) {
            response = ResponseEntity.ok("Match créé");
        } else {
            response = ResponseEntity.status(403).body("Match non créé, manque d'information");
        }

        return response;
    }

    @PostMapping("/admin/modifierMatch")
    public ResponseEntity<String> modifierMatch(@RequestBody MatchRequest request) {
        ResponseEntity<String> response = null;

        Equipe equipe1 = database.getEquipeParId(request.getId_equipe1());
        Equipe equipe2 = database.getEquipeParId(request.getId_equipe2());

        Match match = database.getMatchParId(request.getId_match());
        match.setEquipe1(equipe1);
        match.setEquipe2(equipe2);
        match.setDate_match(request.getDate());
        match.setScore1(request.getScore1());
        match.setScore2(request.getScore2());

        if (database.modifierMatch(match) != null) {
            response = ResponseEntity.ok("Match modifié");
        } else {
            response = ResponseEntity.status(403).body("Match non modifié, manque d'information");
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
}
