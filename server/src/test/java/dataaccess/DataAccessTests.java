package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.Service;
import service.ServiceException;

import java.util.HashMap;
import java.util.Map;

public class DataAccessTests {
    private static Service service;
    private static UserData testUser;
    private static UserData testUser_2;
    private static AuthData testAuth;
    private static AuthData testAuth_2;
    private static GameData testGame;
    private static GameData testGame_2;

    @BeforeAll
    public static void init(){
        service = new Service();
        testUser = new UserData("carl", "balloons", "cliff@mail");
        testUser_2 = new UserData("russell", "balloons", "birds@mail");
        testAuth = new AuthData(service.generateAuthToken(), "Ellie");
        testAuth_2 = new AuthData(service.generateAuthToken(), "Kevin" );
        testGame = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
        testGame_2 = new GameData(
                new ChessGame(), null, null,
                "adventure", 5678
        );
    }

    @BeforeEach
    public void setup() throws ServiceException {
        service.clearDB();
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
    void getAllGames(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        gameDAO.addGame(testGame);
        gameDAO.addGame(testGame_2);
        Map<Integer, GameData> actual = gameDAO.getAllGames();

        Map<Integer, GameData> expected = new HashMap<>();
        expected.put(testGame.gameID(), testGame);
        expected.put(testGame_2.gameID(), testGame_2);

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
        UserDAOMemory userDAO = new UserDAOMemory();
        AuthDAOMemory authDAO = new AuthDAOMemory();
        GameDAOMemory gameDAO = new GameDAOMemory();

        userDAO.addUser(testUser);
        userDAO.addUser(testUser_2);
        authDAO.addAuthData(testAuth);
        authDAO.addAuthData(testAuth_2);
        gameDAO.addGame(testGame);
        gameDAO.addGame(testGame_2);

        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();

        assertEquals(0, userDAO.getNumUsers());
        assertEquals(0, authDAO.getNumAuths());
        assertEquals(0, gameDAO.getNumGames());
    }

}
