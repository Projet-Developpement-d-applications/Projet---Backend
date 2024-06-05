package projet.conquerants.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.*;

import projet.conquerants.Model.Request.StatistiqueRequest;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.ValidationService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class StatistiqueController {

    private DatabaseService database;
    private Logger logger = LoggerFactory.getLogger(StatistiqueController.class);

    @Autowired
    public StatistiqueController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("/admin/ajouterStatistique")
    public ResponseEntity<String> ajouterStatistique(@RequestBody StatistiqueRequest request, HttpServletRequest servletRequest) {
        ResponseEntity<String> response = null;

        try {
            Statistique statistique = database.createStat(creerStatTemp(request));

            if (statistique != null) {
                logger.info("Statistique creer avec succes par " + servletRequest.getRemoteAddr());
                response = ResponseEntity.ok("La statistique a bien été créée");
            } else {
                logger.warn("Echec creation statistique par " + servletRequest.getRemoteAddr());
                ResponseEntity.status(403).body("La statistique n'a pas pu être créée");
            }
        } catch (ManqueInfoException e) {
            logger.warn("Echec creation statistique (wrong info) par " + servletRequest.getRemoteAddr());
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        }

        return response;
    }

    private Statistique creerStatTemp(StatistiqueRequest request) throws RuntimeException {
        Jeu jeu = database.getJeuParNom(request.getJeu());
        Saison saison = database.getSaisonParDebut(request.getSaison());
        Joueur joueur = database.getJoueurParNom(request.getPseudo(), jeu, saison);
        Partie partie = database.getPartieParId(request.getId_partie());

        return new Statistique(request.getDonnee(), joueur, partie);
    }
}
