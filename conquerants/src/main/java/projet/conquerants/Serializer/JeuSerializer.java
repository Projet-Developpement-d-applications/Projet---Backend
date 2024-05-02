package projet.conquerants.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import projet.conquerants.Model.Jeu;

import java.io.IOException;

public class JeuSerializer extends JsonSerializer<Jeu> {
    @Override
    public void serialize(Jeu jeu, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(jeu.getNom());
    }
}
