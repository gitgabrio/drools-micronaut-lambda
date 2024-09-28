package example.micronaut;
import io.micronaut.function.aws.MicronautRequestHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;
import java.util.Collections;

public class FunctionRequestHandler extends MicronautRequestHandler<Measurement, Measurement> {
    @Inject
    JsonMapper objectMapper;

    @Override
    public Measurement execute(Measurement input) {
        System.out.println("Micronaut version: " + io.micronaut.core.version.VersionUtils.MICRONAUT_VERSION);
        try {
            System.out.println("input: " + objectMapper.writeValueAsString(input));
        } catch (IOException e) {
            throw new RuntimeException("Error processing request", e);
        }
        //String json = new String(objectMapper.writeValueAsBytes(Collections.singletonMap("message", "Hello World")));
        //response.setStatusCode(200);
        //response.setBody(input);
        Measurement response = new Measurement("color", "red");
        return input;
    }
}
