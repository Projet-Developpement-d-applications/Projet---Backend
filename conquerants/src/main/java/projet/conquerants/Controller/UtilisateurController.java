package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.Request.UtilisateurRequest;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class UtilisateurController {

    private DatabaseService database;

    @Autowired
    public UtilisateurController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("/utilisateurParPseudo")
    public Utilisateur utilisateurParPseudo(@RequestBody UtilisateurRequest request) {
        Utilisateur retour = null;

        Utilisateur utilisateur = database.getUtilisateurParPseudo(request.getPseudo(), request.getMot_passe());

        if (utilisateur != null) {
            retour = utilisateur;
        }

        return retour;
    }
}