package example.micronaut;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionRequestHandlerTest {

    private static FunctionRequestHandler handler;

    @BeforeAll
    public static void setupServer() {
        handler = new FunctionRequestHandler();
    }

    @AfterAll
    public static void stopServer() {
        if (handler != null) {
            handler.getApplicationContext().close();
        }
    }

    @Test
    public void testHandler() {
        Measurement request = new Measurement("color", "red");

        Measurement response = handler.execute(request);
        assertEquals(response.getVal(), "red");
    }
}
