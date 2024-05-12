package projet.conquerants.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import projet.conquerants.Model.Utilisateur;

import java.io.IOException;

public class UtilisateurSerializer extends JsonSerializer<Utilisateur> {
    @Override
    public void serialize(Utilisateur utilisateur, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(utilisateur.getPseudo());
    }
}
