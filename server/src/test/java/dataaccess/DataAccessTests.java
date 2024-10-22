package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.Service;

public class DataAccessTests {
    private static UserData testUser;
    private static AuthData testAuth;
    private static GameData testGame;

    @BeforeAll
    public static void init(){
        Service service = new Service();
        testUser = new UserData("carl", "balloons", "cliff@mail");
        testAuth = new AuthData(service.generateAuthToken(), "ellie");
        testGame = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
    }

    @Test
    void addAndGetUser(){
        UserDAOMemory dataAccess = new UserDAOMemory();
        UserData expected = testUser;
        dataAccess.addUser(expected);
        UserData actual = dataAccess.getUser(testUser.username());
        assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        AuthDAOMemory dataAccess = new AuthDAOMemory();
        AuthData expected = testAuth;

        dataAccess.addAuthData(expected);
        AuthData actual = dataAccess.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    void addAndGetGamaData(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        GameData expected = testGame;
        gameDAO.addGame(expected);
        GameData actual = gameDAO.getGame(expected.gameID());
        assertEquals(expected, actual);

    }

    @Test
    void deleteAuthData(){
        AuthDAOMemory authDAO = new AuthDAOMemory();
        authDAO.addAuthData(testAuth);
        authDAO.deleteAuth(testAuth);
        assertNull(authDAO.getAuthData(testAuth.authToken()));
    }

    @Test
    void clearDB(){
        UserData user = new UserData(
                "russell", "balloons", "birds@mail"
        );
        AuthData auth = new AuthData(
                "98765", "Kevin"
        );
        GameData game = new GameData(
                new ChessGame(), null, null,
                "adventure", 5678
        );

        UserDAOMemory userDAO = new UserDAOMemory();
        AuthDAOMemory authDAO = new AuthDAOMemory();
        GameDAOMemory gameDAO = new GameDAOMemory();

        userDAO.addUser(user);
        userDAO.addUser(testUser);
        authDAO.addAuthData(auth);
        authDAO.addAuthData(testAuth);
        gameDAO.addGame(game);
        gameDAO.addGame(testGame);

        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();

        assertEquals(0, userDAO.getNumUsers());
        assertEquals(0, authDAO.getNumAuths());
        assertEquals(0, gameDAO.getNumGames());
    }

}
