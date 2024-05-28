package projet.conquerants.Model.Response;

public class AuthenticationResponse implements IResponse {

    private String token;
    private String role;
    private String pseudo;

    public AuthenticationResponse(String token, String pseudo, String role) {
        this.token = token;
        this.role = role;
        this.pseudo = pseudo;
    }

    public AuthenticationResponse(String pseudo, String role) {
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getPseudo() {
        return pseudo;
    }
}
