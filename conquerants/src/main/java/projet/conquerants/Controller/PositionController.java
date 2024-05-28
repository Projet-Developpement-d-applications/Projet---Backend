package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Position;
import projet.conquerants.Model.Request.EquipeRequest;
import projet.conquerants.Model.Request.PositionRequest;
import projet.conquerants.Model.Saison;
import projet.conquerants.Service.DatabaseService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class PositionController {

    private DatabaseService database;

    @Autowired
    public PositionController(DatabaseService database) {
        this.database = database;
    }

    @GetMapping("/noAuth/positions")
    public List<Position> getPositions() {
        return database.getPositions();
    }

    @PostMapping("positionParJeu")
    public List<Position> positionParJeu(@RequestBody PositionRequest request) {
        List<Position> retour = new ArrayList<>();

        Jeu jeu = database.getJeuParNom(request.getJeu());

        List<Position> positions = database.getPositionParJeu(jeu);
        if (!positions.isEmpty()) {
            retour = positions;
        }

        return retour;
    }
}
