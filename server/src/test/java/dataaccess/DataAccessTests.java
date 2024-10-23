package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.Service;
import service.ServiceException;

import java.util.HashMap;
import java.util.Map;

public class DataAccessTests {
    private static Service service;
    private static UserData testUser1;
    private static UserData testUser2;
    private static AuthData testAuth1;
    private static AuthData testAuth2;
    private static GameData testGame1;
    private static GameData testGame2;

    @BeforeAll
    public static void init(){
        service = new Service();
        testUser1 = new UserData("carl", "balloons", "cliff@mail");
        testUser2 = new UserData("russell", "balloons", "birds@mail");
        testAuth1 = new AuthData(service.generateAuthToken(), "Ellie");
        testAuth2 = new AuthData(service.generateAuthToken(), "Kevin" );
        testGame1 = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
        testGame2 = new GameData(
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
        UserData expected = testUser1;
        dataAccess.addUser(expected);
        UserData actual = dataAccess.getUser(testUser1.username());
        assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        AuthDAOMemory dataAccess = new AuthDAOMemory();
        AuthData expected = testAuth1;

        dataAccess.addAuthData(expected);
        AuthData actual = dataAccess.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    void addAndGetGamaData(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        GameData expected = testGame1;
        gameDAO.addGame(expected);
        GameData actual = gameDAO.getGame(expected.gameID());
        assertEquals(expected, actual);

    }

    @Test
    void getAllGames(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        gameDAO.addGame(testGame1);
        gameDAO.addGame(testGame2);
        Map<Integer, GameData> actual = gameDAO.getAllGames();

        Map<Integer, GameData> expected = new HashMap<>();
        expected.put(testGame1.gameID(), testGame1);
        expected.put(testGame2.gameID(), testGame2);

        assertEquals(expected, actual);
    }

    @Test
    void deleteAuthData(){
        AuthDAOMemory authDAO = new AuthDAOMemory();
        authDAO.addAuthData(testAuth1);
        authDAO.deleteAuth(testAuth1);
        assertNull(authDAO.getAuthData(testAuth1.authToken()));
    }

    @Test
    void updateGameRecord(){
        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, "bob");
        System.out.println(updatedGame);
        //should do some equals method here
    }

    @Test
    void updateGameDataAccessLayer(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        gameDAO.addGame(testGame1);
        String expectedUserName = "bob";

        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, expectedUserName);
        gameDAO.updateGame(updatedGame);

        String actualUserName = gameDAO.getGame(testGame1.gameID()).whiteUsername();
        assertEquals(expectedUserName, actualUserName);
    }


    @Test
    void clearDB(){
        UserDAOMemory userDAO = new UserDAOMemory();
        AuthDAOMemory authDAO = new AuthDAOMemory();
        GameDAOMemory gameDAO = new GameDAOMemory();

        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);
        authDAO.addAuthData(testAuth1);
        authDAO.addAuthData(testAuth2);
        gameDAO.addGame(testGame1);
        gameDAO.addGame(testGame2);

        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();

        assertEquals(0, userDAO.getNumUsers());
        assertEquals(0, authDAO.getNumAuths());
        assertEquals(0, gameDAO.getNumGames());
    }

}
