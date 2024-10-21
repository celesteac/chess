package server;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ServerHelperTests {
    private static Server server;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init(){
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

//    @Disabled("this function must be marked as public before the test can run")
//    void checkValidRegisterRequest(){
//        UserData goodUser = new UserData(
//                "abby","kittens", "rainbow@mail"
//        );
//        UserData badUser = new UserData(
//                "zed","soccer", null
//        );
//        assertTrue(server.checkValidRegisterRequest(goodUser));
//        assertFalse(server.checkValidRegisterRequest(badUser));
//    }
}
