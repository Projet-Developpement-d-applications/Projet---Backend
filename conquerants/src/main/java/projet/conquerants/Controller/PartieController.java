package projet.conquerants.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Model.Match;
import projet.conquerants.Model.Partie;
import projet.conquerants.Model.Request.PartieRequest;
import projet.conquerants.Service.DatabaseService;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class PartieController {

    private DatabaseService database;
    private Logger logger = LoggerFactory.getLogger(PartieController.class);

    @Autowired
    public PartieController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("/admin/ajouterPartie")
    public ResponseEntity<String> ajouterPartie(@RequestBody PartieRequest request, HttpServletRequest servletRequest) {
        ResponseEntity<String> response = null;

        try {
            Match match = database.getMatchParId(request.getId_match());

            if (match == null) {
                throw new ExistePasException();
            }

            valideRequest(request);

            Partie partie = new Partie(request.getScore1(), request.getScore2(), match);
            Partie createdPartie = database.createPartie(partie);

            if (createdPartie != null) {
                if (partie.getScore1() > partie.getScore2()) {
                    match.setScore1(match.getScore1() + 1);
                } else {
                    match.setScore2(match.getScore2() + 1);
                }

                database.modifierMatch(match);

                System.out.println(createdPartie.getId());
                String createdPartieID = Integer.toString(createdPartie.getId());
                logger.info("Partie creer avec succes par " + servletRequest.getRemoteAddr());
                response = ResponseEntity.ok(createdPartieID);
            } else {
                logger.warn("Echec creation partie par " + servletRequest.getRemoteAddr());
                response = ResponseEntity.status(403).body("La partie n'a pas pu être créé");
            }
        } catch (ManqueInfoException e) {
            logger.warn("Echec creation partie (wrong info) par " + servletRequest.getRemoteAddr());
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        } catch (ExistePasException e) {
            logger.warn("Echec creation partie (aucun match) par " + servletRequest.getRemoteAddr());
            response = ResponseEntity.status(403).body("Le match auquel vous souhaitez ajouter cette partie n'existe pas");
        }

        return response;
    }

    @PutMapping("/admin/modifierPartie")
    public ResponseEntity<String> modiferPartie(@RequestBody PartieRequest request, HttpServletRequest servletRequest) {
        ResponseEntity<String> response = null;

        try {
            Partie partie = database.getPartieParId(request.getId_partie());
            if (partie == null) {
                throw new ExistePasException();
            }

            valideRequest(request);

            partie.setScore1(request.getScore1());
            partie.setScore2(request.getScore2());

            if (database.modifierPartie(partie) != null) {
                logger.info("Partie modifier avec succes par " + servletRequest.getRemoteAddr());
                response = ResponseEntity.ok("La partie a été modifié avec succès");
            } else {
                logger.warn("Echec modification partie par " + servletRequest.getRemoteAddr());
                response = ResponseEntity.status(403).body("La partie n'a pas pu être modifié");
            }
        } catch (ManqueInfoException e) {
            logger.warn("Echec modification partie (wrong info) par " + servletRequest.getRemoteAddr());
            response = ResponseEntity.status(403).body("Les informations fournies ne sont pas valides");
        } catch (ExistePasException e) {
            logger.warn("Echec modification partie (existe pas) par " + servletRequest.getRemoteAddr());
            response = ResponseEntity.status(403).body("Le partie que vous souhaitez modifié n'existe pas");
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

    private void valideRequest(PartieRequest request) throws ManqueInfoException {
        if (request.getScore1() < 0 || request.getScore2() < 0) {
            throw new ManqueInfoException();
        }
    }


}
