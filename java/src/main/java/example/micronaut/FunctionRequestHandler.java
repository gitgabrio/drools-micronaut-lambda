package example.micronaut;

// Micronaut stuff
import io.micronaut.function.aws.MicronautRequestHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;

// Drools stuff
import org.drools.ruleunits.api.RuleUnitProvider;
import org.drools.ruleunits.api.RuleUnitInstance;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class FunctionRequestHandler extends MicronautRequestHandler<Measurement, Measurement> {
    @Inject
    JsonMapper objectMapper;

    @Override
    public Measurement execute(Measurement input) {
        System.out.println("Micronaut version: " + io.micronaut.core.version.VersionUtils.MICRONAUT_VERSION);
        try {
            System.out.println("input: " + objectMapper.writeValueAsString(input));
        } catch (IOException e) {
            throw new RuntimeException("Error processing input", e);
        }

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.getMeasurements().add(input);
        RuleUnitInstance<MeasurementUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(measurementUnit);
        List<Measurement> queryResult = instance.executeQuery("FindColor").toList("$m");
        instance.close();

        try {
            System.out.println("output: " + objectMapper.writeValueAsString(queryResult));
        } catch (IOException e) {
            throw new RuntimeException("Error processing output", e);
        }

        return queryResult.get(0);
    }
}
