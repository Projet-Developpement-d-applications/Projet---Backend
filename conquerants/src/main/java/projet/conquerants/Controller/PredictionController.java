package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Request.PredictionRequest;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;

import java.util.List;

@RestController
public class PredictionController {

    private DatabaseService database;

    @Autowired
    public PredictionController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("ajouterPrediction")
    public ResponseEntity<String> ajouterPrediction(@RequestBody PredictionRequest request) {
        ResponseEntity<String> response = ResponseEntity.status(403).body("Prediction non créé");

        Utilisateur utilisateur = database.getUtilisateur(request.getPseudo()).get();
        Match match = database.getMatchParId(request.getId_match());

        if (!match.getJouer()) {
            Prediction prediction = new Prediction(request.getVote(), match, utilisateur);
            if (database.createPrediction(prediction) != null) {
                response = ResponseEntity.ok("Prediction créé");
            }
        }

        return response;
    }

    @PostMapping("predictionParUtilisateur")
    public List<Prediction> predictionParUtilisateur(@RequestBody PredictionRequest request) {
        Utilisateur utilisateur = database.getUtilisateur(request.getPseudo()).get();

        return database.getPredictionParUtilisateur(utilisateur);
    }

    @PostMapping("predictionParMatch")
    public List<Prediction> predictionParMatch(@RequestBody PredictionRequest request) {
        Match match = database.getMatchParId(request.getId_match());

        return database.getPredictionParMatch(match);
    }
}
