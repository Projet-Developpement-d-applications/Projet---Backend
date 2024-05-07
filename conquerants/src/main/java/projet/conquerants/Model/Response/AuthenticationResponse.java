package projet.conquerants.Model.Response;

public class AuthenticationResponse implements IResponse{

    private String token;
    private String role;

    public AuthenticationResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}
