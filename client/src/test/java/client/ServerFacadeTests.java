package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import org.junit.jupiter.api.*;
import requestresponsetypes.GameDetails;
import server.ErrorResponse;
import server.Server;

import java.util.ArrayList;


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
    public void registerBadRequest() {
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.register("celeste", null, "piano@email.com"));
    }

    @Test
    public void registerTwiceSameUser(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.register("celeste", "other", "piano@email.com"));
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
        Exception ex = Assertions.assertThrows(ResponseException.class, ()-> serverFacade.register("celeste", "secret", "piano@email.com"));
    }

    @Test
    public void logoutTestBad(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        Exception ex = Assertions.assertThrows(ResponseException.class, ()-> serverFacade.logout(null));
    }

    //how to test logout effectively?
    //tried to get message -> should do that for more of the tests

    @Test
    public void createTestGood(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.createGame("new game", auth.authToken());
        serverFacade.createGame("new game", auth.authToken());
        ArrayList<GameDetails> games =  serverFacade.listGames(auth.authToken());
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void createTestBad(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.createGame(null, auth.authToken()));
    }

    @Test
    public void listBad(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.createGame("new game", auth.authToken());
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.listGames(null));
    }

    @Test
    public void joinGood(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.createGame("new game", auth.authToken());
        ArrayList<GameDetails> games = serverFacade.listGames(auth.authToken());
        serverFacade.joinGame(games.getFirst().gameID(), ChessGame.TeamColor.WHITE, auth.authToken());
        ArrayList<GameDetails> newGames = serverFacade.listGames(auth.authToken());
        Assertions.assertEquals("celeste", newGames.getFirst().whiteUsername());
    }

    @Test
    public void joinBad(){
        serverFacade.register("celeste", "secret", "piano@email.com");
        AuthData auth = serverFacade.login("celeste", "secret");
        serverFacade.createGame("new game", auth.authToken());
        ArrayList<GameDetails> games = serverFacade.listGames(auth.authToken());
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.joinGame(games.getFirst().gameID(), null, auth.authToken()));
    }



}
