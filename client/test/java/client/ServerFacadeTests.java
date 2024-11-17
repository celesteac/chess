package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {
    private static ServerFacade serverFacade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterEach
    void clear(){
        serverFacade.delete();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTestGood() {
        AuthData auth = serverFacade.register("celeste", "secret", "piano@email.com");
        Assertions.assertEquals(auth.username(), "celeste");
    }

    @Test
    public void registerTestBad() {
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.register("celeste", null, "piano@email.com"));
    }

    @Disabled
    public void deleteText(){
        serverFacade.register("miguel", "cute", "piano@email");
        //fix this
    }

}
