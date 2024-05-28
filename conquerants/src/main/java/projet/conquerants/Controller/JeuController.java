package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Jeu;
import projet.conquerants.Service.DatabaseService;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class JeuController {

    private DatabaseService database;

    @Autowired
    public JeuController(DatabaseService database) {
        this.database = database;
    }

    @GetMapping("/noAuth/jeux")
    public List<Jeu> getJeux() {
        return database.getJeux();
    }
}
