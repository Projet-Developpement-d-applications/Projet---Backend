package projet.conquerants.Service;

import org.bouncycastle.crypto.engines.RSAEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private UserDetailsServiceImpl userDetailsService;
    private RSADecoderService rsaDecoderService;

    @Autowired
    public AuthService(DatabaseService databaseService, PasswordEncoder passwordEncoder, JwtService jwtService, RSADecoderService rsaDecoderService,
                       AuthenticationManager authenticationManager, ValidationService validation, UserDetailsServiceImpl userDetailsService) {
        this.databaseService = databaseService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.validation = validation;
        this.userDetailsService = userDetailsService;
        this.rsaDecoderService = rsaDecoderService;
    }

    public boolean checkConnexion(String token) {
        String pseudo = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(pseudo);

        return jwtService.isValid(token, userDetails);
    }

    public AuthenticationResponse refreshConnexion(String token) {
        String pseudo = jwtService.extractUsername(token);

        Utilisateur utilisateur = databaseService.getUtilisateur(pseudo).get();

        return new AuthenticationResponse(jwtService.generateToken(utilisateur), utilisateur.getPseudo(), utilisateur.getRole());
    }

    public AuthenticationResponse inscription(InscriptionRequest request) throws Exception {
        String mdp = rsaDecoderService.decrypt(request.getMot_passe());

        validePseudoExistePas(request.getPseudo());
        valideInfoInscription(request, mdp);

        Utilisateur utilisateur = new Utilisateur(request.getPrenom(), request.getNom(), request.getPseudo(),
                passwordEncoder.encode(mdp), databaseService.getDefaultRole());

        utilisateur = databaseService.createUtilisateur(utilisateur);

        String token = jwtService.generateToken(utilisateur);

        return new AuthenticationResponse(token, utilisateur.getPseudo(), utilisateur.getRole());
    }

    public AuthenticationResponse connexion(ConnexionRequest request) throws Exception {
        String mdp = rsaDecoderService.decrypt(request.getMot_passe());
        valideCredentials(request.getPseudo(), mdp);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getPseudo(),
                mdp));

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

    public void valideInfoInscription(InscriptionRequest request, String mdp) throws ManqueInfoException {
        if (!validation.valideStringOfChar(request.getPrenom()) || !validation.valideStringOfChar(request.getNom()) ||
                !validation.valideStringOfCharAndDigits(request.getPseudo()) || !validation.validePasswordString(mdp)) {
            throw new ManqueInfoException();
        }
    }


}
