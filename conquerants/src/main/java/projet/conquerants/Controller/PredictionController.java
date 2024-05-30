package projet.conquerants.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Prediction;
import projet.conquerants.Model.Request.PredictionRequest;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.JwtService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class PredictionController {

    private DatabaseService database;
    private JwtService jwtService;

    @Autowired
    public PredictionController(DatabaseService database, JwtService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }

    @PostMapping("ajouterPrediction")
    public ResponseEntity<String> ajouterPrediction(@RequestBody PredictionRequest request, HttpServletRequest servletRequest) {
        ResponseEntity<String> response = null;

        try {
            String token = null;
            if (servletRequest.getCookies() != null) {
                for (Cookie cookie : servletRequest.getCookies()) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                    }
                }
                Utilisateur utilisateur = database.getUtilisateur(jwtService.extractUsername(token)).orElse(null);
                Match match = database.getMatchParId(request.getId_match());

                valideExistePas(match, utilisateur);
                valideRequest(request, match, utilisateur);

                if (!match.getJouer()) {
                    Prediction prediction = new Prediction(request.getVote(), match, utilisateur);
                    if (database.createPrediction(prediction) != null) {
                        response = ResponseEntity.status(403).body("La prédiction a été créé avec succès");
                    } else {
                        response = ResponseEntity.status(403).body("La prédiction n'a pas pu être créé");
                    }
                } else {
                    response = ResponseEntity.status(403).body("Le match a déjà été joué, vous ne pouvez pas faire de prédiction");
                }
            }
        } catch (ManqueInfoException | NoSuchElementException e) {
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body("Vous avez déjà fait une prédiction pour ce match");
        }

        return response;
    }

    @GetMapping("predictionParUtilisateur")
    public List<Prediction> predictionParUtilisateur(HttpServletRequest request) {
        String token = null;
        Utilisateur utilisateur = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }

            utilisateur = database.getUtilisateur(jwtService.extractUsername(token)).orElse(null);
        }

        return database.getPredictionParUtilisateur(utilisateur);
    }

    @PostMapping("predictionParMatch")
    public List<Prediction> predictionParMatch(@RequestBody PredictionRequest request) {
        Match match = database.getMatchParId(request.getId_match());

        return database.getPredictionParMatch(match);
    }

    private void valideRequest(PredictionRequest request, Match match, Utilisateur utilisateur) throws ManqueInfoException {
        if (match == null || utilisateur == null) {
            throw new ManqueInfoException();
        }
    }

    private void valideExistePas(Match match, Utilisateur utilisateur) throws ExisteDejaException {
        if (database.getPredictionParUtilisateurEtMatch(utilisateur, match) != null) {
            throw new ExisteDejaException();
        }
    }

    private String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }
}
