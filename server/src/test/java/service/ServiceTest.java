package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import server.CreateRequest;

import java.util.UUID;

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
    public void setup() throws ServiceException{
        service.clearDB();
    }

    @Test
    void registerUser() throws ServiceException {
        // give it a UserData object
        // get back an AuthData with same username
        UserData user = testUser;
        AuthData auth = service.registerUser(user);
        assertEquals(user.username(), auth.username());
        assertEquals(36, auth.authToken().length());
    }

    @Test
    void registerUserAlreadyTaken() throws ServiceException{
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
        System.out.println(uuid);
        Assertions.assertEquals(36, uuid.length());
    }

    @Test
    void login() throws ServiceException{
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
    void loginBadPassword() throws ServiceException{
        service.registerUser(testUser);
        UserData badUser = new UserData(testUser.username(), "wrong", null);
        assertThrows(ServiceException.class, ()->{
            service.login(badUser);
        });
    }

    @Test
    void createGame() throws ServiceException{
        AuthData userAuth = service.registerUser(testUser);
        CreateRequest createReq = new CreateRequest(testGame.gameName(), userAuth.authToken());
        int gameID = service.createGame(createReq);
        String gameIDStr = Integer.toString(gameID);
        System.out.println(gameIDStr);
        assertTrue(gameID > 999 && gameID <10000);
        assertEquals(4, gameIDStr.length());
    }

    @Test
    void createGameNotAuthorized() throws ServiceException{
        CreateRequest createReq = new CreateRequest(testGame.gameName(), testAuth.authToken());
        assertThrows(ServiceException.class, ()->{
            int gameID = service.createGame(createReq);
        });
        //check which status code it's throwing
    }

    @Test
    void createGameBadRequest() throws ServiceException{
        CreateRequest createReq = new CreateRequest(testGame.gameName(), "");
        assertThrows(ServiceException.class, ()->{
            int gameID = service.createGame(createReq);
        });
        //check which status code it's throwing
    }

}
