package projet.conquerants.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Exception.MauvaisMotPasseException;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
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
    private ValidationService validation;

    @Autowired
    public AuthService(DatabaseService databaseService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ValidationService validation) {
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.validation = validation;
    }

    public AuthenticationResponse inscription(InscriptionRequest request) throws RuntimeException {
        validePseudoExistePas(request.getPseudo());
        valideInfoInscription(request);

        Utilisateur utilisateur = new Utilisateur(request.getPrenom(), request.getNom(), request.getPseudo(),
                passwordEncoder.encode(request.getMot_passe()), databaseService.getDefaultRole());

        utilisateur = databaseService.createUtilisateur(utilisateur);

        String token = jwtService.generateToken(utilisateur);

        return new AuthenticationResponse(token, utilisateur.getPseudo(), utilisateur.getRole());
    }

    public AuthenticationResponse connexion(ConnexionRequest request) throws RuntimeException {
        valideCredentials(request.getPseudo(), request.getMot_passe());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPseudo(),
                request.getMot_passe()));

        Utilisateur utilisateur = databaseService.getUtilisateur(request.getPseudo()).orElseThrow();
        String token = jwtService.generateToken(utilisateur);

        return new AuthenticationResponse(token, utilisateur.getPseudo(), utilisateur.getRole());
    }

    public void valideCredentials(String pseudo, String motPasse) throws MauvaisMotPasseException, ExistePasException {
        Utilisateur utilisateur = databaseService.getUtilisateur(pseudo).orElseThrow(ExistePasException::new);

        if (!passwordEncoder.matches(motPasse, utilisateur.getMot_passe())) {
            throw new MauvaisMotPasseException();
        }
    }

    public void validePseudoExistePas(String pseudo) throws ExisteDejaException {
        if (databaseService.getUtilisateur(pseudo).isPresent()) {
            throw new ExisteDejaException();
        }
    }

    public void valideInfoInscription(InscriptionRequest request) throws ManqueInfoException {
        if (!validation.valideStringOfChar(request.getPrenom()) || !validation.valideStringOfChar(request.getNom()) ||
                !validation.valideStringOfCharAndDigits(request.getPseudo()) || !validation.validePasswordString(request.getMot_passe())) {
            throw new ManqueInfoException();
        }
    }


}
