package projet.conquerants.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import projet.conquerants.Model.Position;

import java.io.IOException;

public class PositionSerializer extends JsonSerializer<Position> {
    @Override
    public void serialize(Position position, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(position.getNom());
    }
}
