package projet.conquerants.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projet.conquerants.Exception.ManqueInfoException;
import projet.conquerants.Exception.MauvaisMotPasseException;
import projet.conquerants.Exception.ExisteDejaException;
import projet.conquerants.Exception.ExistePasException;
import projet.conquerants.Model.Request.ConnexionRequest;
import projet.conquerants.Model.Request.InscriptionRequest;
import projet.conquerants.Model.Response.AuthenticationResponse;
import projet.conquerants.Model.Response.ExceptionResponse;
import projet.conquerants.Model.Response.IResponse;
import projet.conquerants.Model.Response.AuthResponse;
import projet.conquerants.Service.AuthService;
import projet.conquerants.Service.RSADecoderService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class AuthController {

    private AuthService authService;
    private final String COOKIE_NAME = "token";
    private final int COOKIE_EXPIRATION = (30 * 24 * 60 * 60);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/connexionStatus")
    public ResponseEntity<Boolean> connexionStatus(HttpServletRequest request, HttpServletResponse servletResponse) {
        ResponseEntity<Boolean> response = ResponseEntity.ok(false);

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    token = cookie.getValue();
                }
            }

            if (authService.checkConnexion(token)) {
                response = ResponseEntity.ok(true);
                logger.info("Check status approuvé pour " + request.getRemoteAddr());

            } else {
                logger.info("Check status reffusé pour " + request.getRemoteAddr());

                Cookie cookie = createCookie(null, 0);
                servletResponse.addCookie(cookie);
                servletResponse.setHeader("Set-Cookie", String.format(
                        "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                        cookie.getName(),
                        cookie.getValue(),
                        Integer.toString(0),
                        cookie.getMaxAge()
                ));
            }
        }

        return response;
    }

    @GetMapping("deconnexion")
    public ResponseEntity<String> deconnexion(HttpServletResponse servletResponse, HttpServletRequest request) {
        ResponseEntity<String> response = null;

        logger.info("Deconnexion de " + request.getRemoteAddr());

        Cookie cookie = createCookie(null, 0);
        servletResponse.addCookie(cookie);
        servletResponse.setHeader("Set-Cookie", String.format(
                "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                cookie.getName(),
                cookie.getValue(),
                Integer.toString(0),
                cookie.getMaxAge()
        ));

        return ResponseEntity.ok("Vous avez été deconnecté");
    }

    @GetMapping("refreshConnexion")
    public ResponseEntity<IResponse> refreshConnexion(HttpServletResponse servletResponse, HttpServletRequest request) {
        ResponseEntity<IResponse> response = null;

        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    token = cookie.getValue();
                }
            }

            AuthenticationResponse rep = authService.refreshConnexion(token);

            if (rep != null) {
                logger.warn("Échec de la tentative de refresh pour " + request.getRemoteAddr());

                Cookie cookie = createCookie(rep.getToken(), COOKIE_EXPIRATION);
                servletResponse.addCookie(cookie);
                servletResponse.setHeader("Set-Cookie", String.format(
                        "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                        cookie.getName(),
                        cookie.getValue(),
                        Integer.toString(COOKIE_EXPIRATION),
                        cookie.getMaxAge()
                ));

                logger.info("Tentative de refresh réussie pour " + request.getRemoteAddr());
                response = ResponseEntity.ok().body(new AuthResponse(rep.getRole(), rep.getPseudo()));
            }
        }

        return response;
    }

    @PostMapping("/connexion")
    public ResponseEntity<IResponse> login(@RequestBody ConnexionRequest connexionRequest, HttpServletResponse servletResponse, HttpServletRequest request) {
        ResponseEntity<IResponse> response = null;

        try {
            AuthenticationResponse rep = authService.connexion(connexionRequest);

            Cookie cookie = createCookie(rep.getToken(), COOKIE_EXPIRATION);
            servletResponse.addCookie(cookie);
            servletResponse.setHeader("Set-Cookie", String.format(
                    "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                    cookie.getName(),
                    cookie.getValue(),
                    Integer.toString(30 * 24 * 60 * 60),
                    cookie.getMaxAge()
            ));

            logger.info("Tentative de login réussie pour " + request.getRemoteAddr());
            response = ResponseEntity.ok(new AuthResponse(rep.getRole(), rep.getPseudo()));
        } catch (ExistePasException | MauvaisMotPasseException e) {
            logger.warn("Échec de login (bad credentials) pour " + request.getRemoteAddr());
            response = ResponseEntity.status(403).body(new ExceptionResponse("Le pseudonyme ou le mot de passe est invalide"));
        } catch (Exception e) {
            logger.warn("Échec de login (wrong info) pour " + request.getRemoteAddr());
            response = ResponseEntity.status(403).body(new ExceptionResponse("Les informations fournies ne sont pas conformes"));
        }

        return response;
    }

    @PostMapping("/inscription")
    public ResponseEntity<IResponse> register(@RequestBody InscriptionRequest inscriptionRequest, HttpServletResponse servletResponse, HttpServletRequest request) {
        ResponseEntity<IResponse> response = null;

        try {
            AuthenticationResponse rep = authService.inscription(inscriptionRequest);

            Cookie cookie = createCookie(rep.getToken(), COOKIE_EXPIRATION);
            servletResponse.addCookie(cookie);
            servletResponse.setHeader("Set-Cookie", String.format(
                    "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                    cookie.getName(),
                    cookie.getValue(),
                    Integer.toString(COOKIE_EXPIRATION),
                    cookie.getMaxAge()
            ));

            logger.info("Tentative d'inscription réussie pour " + request.getRemoteAddr());
            response = ResponseEntity.ok(new AuthResponse(rep.getRole(), rep.getPseudo()));
        } catch (ManqueInfoException e) {
            logger.warn("Échec de la tentative d'inscription (wrong info) pour " + request.getRemoteAddr());
            response = ResponseEntity.status(403).body(new ExceptionResponse("Les informations fournies ne sont pas conforme"));
        } catch (ExisteDejaException e) {
            logger.warn("Échec de la tentative d'inscription (email used) pour " + request.getRemoteAddr());
            response = ResponseEntity.status(403).body(new ExceptionResponse("Ce pseudonyme est déjà utilisé"));
        } catch (Exception e) {
            logger.warn("Échec de la tentative d'inscription (wrong info) pour " + request.getRemoteAddr());
            response = ResponseEntity.status(403).body(new ExceptionResponse("Les informations fournies ne sont pas conformes"));
        }

        return response;
    }

    private Cookie createCookie(String token, int expiration) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(expiration);

        return cookie;
    }
}
