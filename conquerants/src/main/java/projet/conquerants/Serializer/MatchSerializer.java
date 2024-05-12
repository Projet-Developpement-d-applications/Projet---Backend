package projet.conquerants.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import projet.conquerants.Model.Match;

import java.io.IOException;

public class MatchSerializer extends JsonSerializer<Match> {
    @Override
    public void serialize(Match match, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(match.getId());
    }
}
