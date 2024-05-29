package projet.conquerants.Model.Response;

public class AuthResponse implements IResponse{

    private String role;
    private String pseudo;

    public AuthResponse(String role, String pseudo) {
        this.role = role;
        this.pseudo = pseudo;
    }

    public String getRole() {
        return role;
    }

    public String getPseudo() {
        return pseudo;
    }
}
