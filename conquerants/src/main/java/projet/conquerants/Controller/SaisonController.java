package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Model.Saison;
import projet.conquerants.Service.DatabaseService;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class SaisonController {

    private DatabaseService database;

    @Autowired
    public SaisonController(DatabaseService database) {
        this.database = database;
    }

    @GetMapping("/noAuth/saisons")
    public List<Saison> getSaisons() {
        return database.getSaisons();
    }
}
