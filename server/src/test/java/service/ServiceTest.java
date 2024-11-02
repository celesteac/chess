package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import server.CreateRequest;
import server.JoinRequest;
import server.JoinResponse;

import java.util.*;

public class ServiceTest {
    private static Service service;
    private static UserData testUser;
    private static GameData testGame;
    private static AuthData testAuth;

    @BeforeAll
    public static void init(){
        service = new Service();
        testAuth = new AuthData(service.generateAuthToken(), "ellie");
        testUser = new UserData("miguel", "cute", "the cutest");
        testGame = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
    }

    @BeforeEach
    public void setup() throws ServiceException, DataAccessException {
        service.clearDB();
    }

    @Test
    public void dataAccessType(){

    }

    @Test
    void registerUser() throws ServiceException, DataAccessException {
        // give it a UserData object
        // get back an AuthData with same username
        UserData user = testUser;
        AuthData auth = service.registerUser(user);
        assertEquals(user.username(), auth.username());
        assertEquals(36, auth.authToken().length());
    }

    @Test
    void registerUserAlreadyTaken() throws ServiceException, DataAccessException{
        UserData user = testUser;
        service.registerUser(user);
        assertThrows(ServiceException.class, ()->{
           service.registerUser(user);
        });
    }

    @Test
    void registerUserBadRequest() throws ServiceException{
        UserData badUser = new UserData("car", "balloons", null);
        assertThrows(ServiceException.class, ()->{
            service.registerUser(badUser);
        });
    }

    @Test
    void getNewAuthToken(){
        String uuid = service.generateAuthToken();
//        System.out.println(uuid);
        Assertions.assertEquals(36, uuid.length());
    }

    @Test
    void login() throws ServiceException, DataAccessException{
        service.registerUser(testUser);
        AuthData expected = service.login(testUser);
        assertNotNull(expected);
    }

    @Test
    void loginUnauthorized() throws ServiceException{
        assertThrows(ServiceException.class, ()->{
            service.login(testUser);
        });
    }

    @Test
    void loginBadPassword() throws ServiceException, DataAccessException{
        service.registerUser(testUser);
        UserData badUser = new UserData(testUser.username(), "wrong", null);
        assertThrows(ServiceException.class, ()->{
            service.login(badUser);
        });
    }

    @Test
    void createGame() throws ServiceException, DataAccessException{
        AuthData userAuth = service.registerUser(testUser);
        CreateRequest createReq = new CreateRequest(testGame.gameName(), userAuth.authToken());
        int gameID = service.createGame(createReq);
        String gameIDStr = Integer.toString(gameID);
//        System.out.println(gameIDStr);
        assertTrue(gameID > 999 && gameID <10000);
        assertEquals(4, gameIDStr.length());
    }

    @Test
    void createGameNotAuthorized() throws ServiceException{
        CreateRequest createReq = new CreateRequest(testGame.gameName(), testAuth.authToken());
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            int gameID = service.createGame(createReq);
            //throws 401 unauthorized
        });
        assertEquals(401, ex.getStatus());
    }

    @Test
    void createGameBadRequest() throws ServiceException{
        CreateRequest createReq = new CreateRequest(testGame.gameName(), "");
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            int gameID = service.createGame(createReq);
            //throws 400 bad request
        });
        assertEquals(400, ex.getStatus());
    }

    @Test
    void logout() throws ServiceException, DataAccessException{
        AuthData userAuth = service.registerUser(testUser);
        service.logout(userAuth.authToken());
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            CreateRequest createReq = new CreateRequest("cool game", userAuth.authToken());
            service.createGame(createReq);
            //throws 401 unauthorized
        });
        assertEquals(401, ex.getStatus());
    }

    @Test
    void logoutUnauthorized(){
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            service.logout(testAuth.authToken());
            //throws 401 unauthorized
        });
        assertEquals(401, ex.getStatus());
    }

    @Test
        //not a robust test
    void listGames() throws ServiceException, DataAccessException{
        AuthData userAuth = service.registerUser(testUser);
        CreateRequest createReq1 = new CreateRequest("game 1", userAuth.authToken());
        CreateRequest createReq2 = new CreateRequest("game 2", userAuth.authToken());
        service.createGame(createReq1);
        service.createGame(createReq2);
        ListResponse games = service.listGames(userAuth.authToken());
        assertEquals(2, games.games().size());
    }

    @Test
    void listGamesUnauthorized(){
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            service.listGames(testAuth.authToken());
            //throws 401 unauthorized
        });
        assertEquals(401, ex.getStatus());
    }

    @Test
    void joinGame() throws ServiceException, DataAccessException{
        AuthData userAuth = service.registerUser(testUser);
        int gameID = service.createGame(new CreateRequest("cool game", userAuth.authToken()));
        JoinRequest joinReq = new JoinRequest(ChessGame.TeamColor.WHITE, gameID, userAuth.authToken());
        int gameIDRes = service.joinGame(joinReq);
        assertEquals(gameID, gameIDRes);
    }

    //missing test for joinGamePlayerTaken

    @Test
    void joinGameDoesNotExist() throws ServiceException, DataAccessException{
        AuthData userAuth = service.registerUser(testUser);
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            service.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, 1234, userAuth.authToken()));
        });
        assertEquals(500, ex.getStatus());
    }

    @Test
    void joinGameUnauthorized(){
        JoinRequest joinReq = new JoinRequest(ChessGame.TeamColor.WHITE, 1234, testAuth.authToken());
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            service.joinGame(joinReq);
        });
        assertEquals(401, ex.getStatus());
    }

    @Test
    void joinGameBadRequest(){
        JoinRequest joinReq = new JoinRequest(ChessGame.TeamColor.WHITE, null, testAuth.authToken());
        ServiceException ex = assertThrows(ServiceException.class, ()->{
            service.joinGame(joinReq);
        });
        assertEquals(400, ex.getStatus());
    }

    @Test
    void compareEncryptedPasswords(){
        String password1 = "happy";
        String password2 = "ecstatic";
        String password1Encrypted = service.getEncryptedPassword(password1);
        assertFalse(service.comparePasswords(password1Encrypted, password2));
        assertTrue(service.comparePasswords(password1Encrypted, password1));
    }
}
