package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Request.ConnexionRequest;
import projet.conquerants.Request.InscriptionRequest;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;

import java.util.Objects;

import static projet.conquerants.Util.PasswordHashUtil.hashPassword;

@RestController
public class AuthController {

    private DatabaseService database;

    @Autowired
    public AuthController(DatabaseService database) {
        this.database = database;
    }

    @PostMapping("connexion")
    public String login(@RequestBody ConnexionRequest connexionRequest) {
        String retour = "non";

        Utilisateur utilisateur = database.getUtilisateur(connexionRequest.getPseudo());
        if (utilisateur != null) {
            String hashMotPasse = hashPassword(connexionRequest.getMot_passe());
            if (Objects.equals(hashMotPasse, utilisateur.getMot_passe())) {
                retour = "oui";
            } else {
                retour = "mauvais mdp";
            }
        }

        return retour;
    }

    @PostMapping("inscription")
    public String register(@RequestBody InscriptionRequest inscriptionRequest) {
        String retour = "non";

        if (database.getUtilisateur(inscriptionRequest.getPseudo()) == null) {
            Utilisateur result = database.createUtilisateur(creerUtilisateurTemp(inscriptionRequest));
            retour = result != null ? "oui" : "n'a pas pu être créé";
        } else {
            retour = "utilisateur avec ce pseudo existe déjà";
        }

        return retour;
    }

    /** ajout validation*/
    private Utilisateur creerUtilisateurTemp(InscriptionRequest inscriptionRequest) {
        return new Utilisateur(inscriptionRequest.getPrenom(), inscriptionRequest.getNom(), inscriptionRequest.getPseudo(),
                hashPassword(inscriptionRequest.getMot_passe()), database.getDefaultRole());
    }



}
