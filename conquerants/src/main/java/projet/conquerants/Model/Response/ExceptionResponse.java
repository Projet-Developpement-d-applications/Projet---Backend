package projet.conquerants.Model.Response;

public class ExceptionResponse implements IResponse{

    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
