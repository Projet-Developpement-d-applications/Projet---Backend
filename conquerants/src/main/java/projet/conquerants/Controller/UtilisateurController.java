package projet.conquerants.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Model.Request.UtilisateurRequest;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.JwtService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class UtilisateurController {

    private DatabaseService database;
    private JwtService jwtService;

    @Autowired
    public UtilisateurController(DatabaseService database, JwtService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }

    @GetMapping("/utilisateurInfo")
    public Utilisateur utilisateurParPseudo(HttpServletRequest request) {
        String token = null;
        Utilisateur retour = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
            Utilisateur utilisateur = database.getUtilisateur(jwtService.extractUsername(token)).orElse(null);

            if (utilisateur != null) {
                retour = utilisateur;
            }

        }
        return retour;
    }
}
