package projet.conquerants.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Model.LoginRequest;
import projet.conquerants.Model.RegisterRequest;
import projet.conquerants.Model.Utilisateur;

import java.util.Objects;

import static projet.conquerants.Util.PasswordHashUtil.hashPassword;

@RestController
public class ConnexionController {

    private DatabaseController database;

    public ConnexionController() {
        database = new DatabaseController();
    }

    @PostMapping("connexion")
    public String login(@RequestBody LoginRequest loginRequest) {
        String retour = "non";

        Utilisateur realInfo = database.doesUserExist(loginRequest.getPseudo());
        if (realInfo != null) {
            String hashMotPasse = hashPassword(loginRequest.getMot_passe());
            if (Objects.equals(hashMotPasse, realInfo.getMot_passe())) {
                retour = "oui";
            } else {
                retour = "mauvais mdp";
            }
        }

        return retour;
    }

    @PostMapping("inscription")
    public String register(@RequestBody RegisterRequest registerRequest) {
        String retour = "non";

        if (database.doesUserExist(registerRequest.getPseudo()) == null) {
            boolean result = database.createUser(registerRequest);

            retour = result ? "oui" : "n'a pas pu être créé";
        } else {
            retour = "utilisateur avec ce pseudo existe déjà";
        }

        return retour;
    }



}
