package projet.conquerants.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Exception.MauvaisMotPasseException;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Model.Request.ConnexionRequest;
import projet.conquerants.Model.Request.InscriptionRequest;
import projet.conquerants.Model.Response.ExceptionResponse;
import projet.conquerants.Model.Response.IResponse;
import projet.conquerants.Service.AuthService;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/connexion")
    public ResponseEntity<IResponse> login(@RequestBody ConnexionRequest connexionRequest) {
        ResponseEntity<IResponse> response = null;

        try {
            response = ResponseEntity.ok(authService.connexion(connexionRequest));
        } catch (ExistePasException | MauvaisMotPasseException e) {
            response = ResponseEntity.status(403).body(new ExceptionResponse("Le pseudonyme ou le mot de passe est invalide"));
        }

        return response;
    }

    @PostMapping("/inscription")
    public ResponseEntity<IResponse> register(@RequestBody InscriptionRequest inscriptionRequest) {
        ResponseEntity<IResponse> response = null;

        try {
            response = ResponseEntity.ok(authService.inscription(inscriptionRequest));
        } catch (ManqueInfoException e) {
            response = ResponseEntity.status(403).body(new ExceptionResponse("Les informations fournies ne sont pas conforme"));
        } catch (ExisteDejaException e) {
            response = ResponseEntity.status(403).body(new ExceptionResponse("Ce pseudonyme est déjà utilisé"));
        }

        return response;
    }
}
