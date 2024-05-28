package projet.conquerants.Model.Response;

public class RoleResponse implements IResponse{

    private String role;

    public RoleResponse(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
