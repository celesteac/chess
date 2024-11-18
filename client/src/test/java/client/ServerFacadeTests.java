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
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    void clear(){
        serverFacade.delete();
    }

    @AfterAll
    static void stopServer() {
        serverFacade.delete();
        server.stop();
    }

    /// TESTS ////////////////////

    @Test
    public void registerTestGood() {
        AuthData auth = serverFacade.register("celeste", "secret", "piano@email.com");
        Assertions.assertEquals(auth.username(), "celeste");
    }

    @Test
    public void registerTestBad() {
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.register("celeste", null, "piano@email.com"));
    }

    @Test
    public void loginTestGood(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        Assertions.assertEquals(auth.username(), "celeste");
    }

    @Test
    public void loginTestBad(){
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.login("celeste", "wrongPassword"));
    }

    @Test
    public void logoutTestGood(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.logout(auth.authToken());
//        Assertions.assertDoesNotThrow(ResponseException.class, ()-> );
    }

    @Test
    public void createTestGood(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.createGame("new game", auth.authToken());
//        serverFacade.
    }


}
