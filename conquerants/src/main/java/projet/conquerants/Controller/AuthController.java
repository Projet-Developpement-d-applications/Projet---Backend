package projet.conquerants.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import projet.conquerants.Model.Response.RoleResponse;
import projet.conquerants.Service.AuthService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://projet-web-acac.vercel.app"}, allowCredentials = "true")
public class AuthController {

    private AuthService authService;
    private final String COOKIE_NAME = "token";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("deconnexion")
    public ResponseEntity<String> deconnexion(HttpServletResponse servletResponse) {
        ResponseEntity<String> response = null;

        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);

        return ResponseEntity.ok("Vous avez été deconnecté");
    }

    @GetMapping("refreshConnexion")
    public ResponseEntity<IResponse> refreshConnexion(HttpServletResponse servletResponse, HttpServletRequest request) {
        ResponseEntity<IResponse> response = null;

        String token = null;

        for(Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                token = cookie.getValue();
            }
        }

        AuthenticationResponse rep = authService.refreshConnexion(token);

        if (rep != null) {
            Cookie cookie = createCookie(rep.getToken());
            servletResponse.addCookie(cookie);
            servletResponse.setHeader("Set-Cookie", String.format("%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure",
                    cookie.getName(), cookie.getValue(), Integer.toString(30 * 24 * 60 * 60), cookie.getMaxAge()));

            response = ResponseEntity.ok().body(new RoleResponse(rep.getRole()));
        } else {
            response = ResponseEntity.status(403).body(new ExceptionResponse("Erreur lors du rafraichissement de la connexion"));
        }

        return response;
    }

    @PostMapping("/connexion")
    public ResponseEntity<IResponse> login(@RequestBody ConnexionRequest connexionRequest, HttpServletResponse servletResponse) {
        ResponseEntity<IResponse> response = null;

        try {
            AuthenticationResponse rep = authService.connexion(connexionRequest);

            Cookie cookie = createCookie(rep.getToken());
            servletResponse.addCookie(cookie);
            servletResponse.setHeader("Set-Cookie", String.format(
                    "%s=%s; Expires=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                    cookie.getName(), // Name of the cookie
                    cookie.getValue(), // Value of the cookie
                    Integer.toString(30 * 24 * 60 * 60), // Expires in 30 days (converted to seconds)
                    cookie.getMaxAge() // Maximum age of the cookie in seconds
            ));

            response = ResponseEntity.ok(new RoleResponse(rep.getRole()));
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

    private Cookie createCookie(String token) {
        LocalDateTime expiration = LocalDateTime.now().plusMonths(1);
        Date expirationDate = Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant());
        int tempsExpiration = (int) ((expirationDate.getTime() - System.currentTimeMillis()) / 1000);

        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(tempsExpiration);

        return cookie;
    }
}
