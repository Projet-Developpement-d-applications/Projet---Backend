package projet.conquerants.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Exception.MauvaisMotPasseException;
import projet.conquerants.Exception.PseudoExisteDejaException;
import projet.conquerants.Exception.UtilisateurExistePasException;
import projet.conquerants.Model.Request.ConnexionRequest;
import projet.conquerants.Model.Response.AuthenticationResponse;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Model.Request.InscriptionRequest;

@Service
public class AuthService {

    private DatabaseService databaseService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(DatabaseService databaseService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse inscription(InscriptionRequest request) throws RuntimeException {
        valideInfoInscription(request);
        validePseudoExistePas(request.getPseudo());

        Utilisateur utilisateur = new Utilisateur(request.getPrenom(), request.getNom(), request.getPseudo(),
                passwordEncoder.encode(request.getMot_passe()), databaseService.getDefaultRole());

        utilisateur = databaseService.createUtilisateur(utilisateur);

        String token = jwtService.generateToken(utilisateur);

        return new AuthenticationResponse(token, utilisateur.getRole());
    }

    public AuthenticationResponse connexion(ConnexionRequest request) throws RuntimeException {
        valideCredentials(request.getPseudo(), request.getMot_passe());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPseudo(),
                request.getMot_passe()));

        Utilisateur utilisateur = databaseService.getUtilisateur(request.getPseudo()).orElseThrow();
        String token = jwtService.generateToken(utilisateur);

        return new AuthenticationResponse(token, utilisateur.getRole());
    }

    private void valideCredentials(String pseudo, String motPasse) throws MauvaisMotPasseException, UtilisateurExistePasException{
        Utilisateur utilisateur = databaseService.getUtilisateur(pseudo).orElseThrow(UtilisateurExistePasException::new);

        if (!passwordEncoder.matches(motPasse, utilisateur.getMot_passe())) {
            throw new MauvaisMotPasseException();
        }
    }

    private void validePseudoExistePas(String pseudo) throws PseudoExisteDejaException{
        if (databaseService.getUtilisateur(pseudo).isPresent()) {
            throw new PseudoExisteDejaException();
        }
    }

    private void valideInfoInscription(InscriptionRequest request) throws ManqueInfoException {
        if (!valideString(request.getPrenom()) || !valideString(request.getNom()) || !valideString(request.getPseudo()) || !valideString(request.getMot_passe())) {
            throw new ManqueInfoException();
        }
    }

    private boolean valideString(String value) {
        return (!value.isEmpty());
    }
}
