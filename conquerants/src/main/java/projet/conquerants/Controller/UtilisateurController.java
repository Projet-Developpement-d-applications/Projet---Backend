package projet.conquerants.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Model.Request.ModifUtilisateurRequest;
import projet.conquerants.Model.Response.UtilisateurResponse;
import projet.conquerants.Model.Utilisateur;
import projet.conquerants.Service.DatabaseService;
import projet.conquerants.Service.JwtService;
import projet.conquerants.Service.RSADecoderService;
import projet.conquerants.Service.ValidationService;

import java.security.PrivateKey;
import java.util.Objects;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class UtilisateurController {

    private DatabaseService database;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private ValidationService validationService;
    private RSADecoderService rsaDecoderService;
    private Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    @Autowired
    public UtilisateurController(DatabaseService database, JwtService jwtService, PasswordEncoder passwordEncoder,
                                 ValidationService validationService, RSADecoderService rsaDecoderService) {
        this.database = database;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
        this.rsaDecoderService = rsaDecoderService;
    }

    @GetMapping("/utilisateurInfo")
    public UtilisateurResponse utilisateurParPseudo(HttpServletRequest request) {
        String token = null;
        UtilisateurResponse retour = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
            Utilisateur utilisateur = database.getUtilisateur(jwtService.extractUsername(token)).orElse(null);

            if (utilisateur != null) {
                retour = new UtilisateurResponse(utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getPseudo(), utilisateur.getRole());
            }

        }
        return retour;
    }

    @PutMapping("/modifierUtilisateur")
    public ResponseEntity<String> modifierUtilisateur(HttpServletRequest request, @RequestBody ModifUtilisateurRequest modifRequest) {
        String token = null;
        ResponseEntity<String> retour = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
            Utilisateur utilisateur = database.getUtilisateur(jwtService.extractUsername(token)).orElse(null);

            if (utilisateur != null) {
                try {
                    String ancienMdp = rsaDecoderService.decrypt(modifRequest.getAncien_mdp());
                    String nouveauMdp = rsaDecoderService.decrypt(modifRequest.getNouveau_mdp());

                    if (!Objects.equals(ancienMdp, nouveauMdp)) {
                        if (validationService.validePasswordString(nouveauMdp)) {
                            if (passwordEncoder.matches(ancienMdp, utilisateur.getPassword())) {
                                utilisateur.setMotPasse(passwordEncoder.encode(nouveauMdp));

                                if (database.modifierUtilisateur(utilisateur) != null) {
                                    logger.info("Modification du mdp pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                                    retour = ResponseEntity.ok("Votre mot de passe a bien été modifié.");
                                } else {
                                    logger.warn("Echec modification du mdp pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                                    retour = ResponseEntity.status(403).body("Le mot de passe n'a pu être modifié.");
                                }
                            } else {
                                logger.warn("Echec modification du mdp (bad mdp) pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                                retour = ResponseEntity.status(403).body("Vous n'avez pas entré le bon mot de passe.");
                            }
                        } else {
                            logger.warn("Echec modification du mdp (nouveau pas conforme) pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                            retour = ResponseEntity.status(403).body("Votre nouveau mot de passe ne respecte pas les critères");
                        }
                    } else {
                        logger.warn("Echec modification du mdp (nouveau = ancien) pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                        retour = ResponseEntity.status(403).body("Votre nouveau mot de passe n'est pas différent de l'ancien");
                    }
                } catch (Exception e) {
                    logger.warn("Echec modification du mdp (wrong info) pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                    retour = ResponseEntity.status(403).body("Vous n'avez pas envoyé des informations valides.");
                    e.printStackTrace();
                }
            } else {
                logger.warn("Echec modification du mdp (bad auth) pour " + utilisateur.getPseudo() + " par " + request.getRemoteAddr());
                retour = ResponseEntity.status(401).body("Vous n'êtes pas authentifié correctement.");
            }
        }

        return retour;
    }
}
