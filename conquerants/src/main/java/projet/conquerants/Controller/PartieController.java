package projet.conquerants.Controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;
import projet.conquerants.Model.Request.PartieRequest;
import projet.conquerants.Service.DatabaseService;

import java.util.List;

@RestController
public class PartieController {

    private DatabaseService database;

    @Autowired
    public PartieController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("/admin/ajouterPartie")
    public ResponseEntity<String> ajouterPartie(@RequestBody PartieRequest request) {
        ResponseEntity<String> response = null;
        Match match = database.getMatchParId(request.getId_match());

        Partie partie = new Partie(request.getScore1(), request.getScore2(), match);

        if (database.createPartie(partie) != null) {
            if (partie.getScore1() > partie.getScore2()) {
                match.setScore1(match.getScore1() + 1);
            } else {
                match.setScore2(match.getScore2() + 1);
            }

            database.modifierMatch(match);

            response = ResponseEntity.ok("Partie créé");
        } else {
            response = ResponseEntity.status(403).body("Partie non créé");
        }

        return response;
    }

    @PutMapping("/admin/modifierPartie")
    public ResponseEntity<String> modiferPartie(@RequestBody PartieRequest request) {
        ResponseEntity<String> response = null;
        Match match = database.getMatchParId(request.getId_match());

        Partie partie = database.getPartieParId(request.getId_partie());
        partie.setScore1(request.getScore1());
        partie.setScore2(request.getScore2());

        if (database.modifierPartie(partie) != null) {

            response = ResponseEntity.ok("Partie modifié");
        } else {
            response = ResponseEntity.status(403).body("Partie non modifié");
        }

        return response;
    }

    @PostMapping("/admin/supprimerPartie")
    public void supprimerPartie(@RequestBody PartieRequest request) {
        Partie partie = database.getPartieParId(request.getId_partie());
        database.deletePartie(partie);
    }

    @PostMapping("partiesParMatch")
    public List<Partie> partiesParMatch(@RequestBody PartieRequest request) {
        Match match = database.getMatchParId(request.getId_match());

        return database.getPartiesParMatch(match);
    }

    @PostMapping("partieParId")
    public Partie partieParId(@RequestBody PartieRequest request) {
        return database.getPartieParId(request.getId_partie());
    }


}
