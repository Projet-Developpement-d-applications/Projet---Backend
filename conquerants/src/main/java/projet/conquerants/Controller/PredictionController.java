package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Request.PredictionRequest;
import projet.conquerants.Service.DatabaseService;

@RestController
public class PredictionController {

    private DatabaseService database;

    @Autowired
    public PredictionController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("ajouterPrediction")
    public Prediction ajouterPrediction(@RequestBody PredictionRequest request) {


    }
}
