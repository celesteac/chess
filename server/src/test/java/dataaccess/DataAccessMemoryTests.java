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

public class DataAccessMemoryTests {
    private static Service service;
    private static UserData testUser1;
    private static UserData testUser2;
    private static AuthData testAuth1;
    private static AuthData testAuth2;
    private static GameData testGame1;
    private static GameData testGame2;
    private static final UserDAOMemory userDataAccess = new UserDAOMemory();
    private static final AuthDAOMemory authDataAccess = new AuthDAOMemory();
    private static final GameDAOMemory gameDataAccess = new GameDAOMemory();

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
    public void setup() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }

    @Test
    void addAndGetUser(){
        UserData expected = testUser1;
        userDataAccess.addUser(expected);
        UserData actual = userDataAccess.getUser(testUser1.username());
        assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        AuthData expected = testAuth1;

        authDataAccess.addAuthData(expected);
        AuthData actual = authDataAccess.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    void addAndGetGamaData(){
        GameData expected = testGame1;
        gameDataAccess.addGame(expected);
        GameData actual = gameDataAccess.getGame(expected.gameID());
        assertEquals(expected, actual);

    }

    @Test
    void getAllGames(){
        gameDataAccess.addGame(testGame1);
        gameDataAccess.addGame(testGame2);
        Map<Integer, GameData> actual = gameDataAccess.getAllGames();

        Map<Integer, GameData> expected = new HashMap<>();
        expected.put(testGame1.gameID(), testGame1);
        expected.put(testGame2.gameID(), testGame2);

        assertEquals(expected, actual);
    }

    @Test
    void deleteAuthData(){
        authDataAccess.addAuthData(testAuth1);
        authDataAccess.deleteAuth(testAuth1);
        assertNull(authDataAccess.getAuthData(testAuth1.authToken()));
    }

    @Test
    void updateGameRecord(){
        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, "bob");
        System.out.println(updatedGame);
        //should do some equals method here
    }

    @Test
    void updateGameDataAccessLayer(){
        gameDataAccess.addGame(testGame1);
        String expectedUserName = "bob";

        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, expectedUserName);
        gameDataAccess.updateGame(updatedGame);

        String actualUserName = gameDataAccess.getGame(testGame1.gameID()).whiteUsername();
        assertEquals(expectedUserName, actualUserName);
    }


    @Test
    void clearDB(){
        userDataAccess.addUser(testUser1);
        userDataAccess.addUser(testUser2);
        authDataAccess.addAuthData(testAuth1);
        authDataAccess.addAuthData(testAuth2);
        gameDataAccess.addGame(testGame1);
        gameDataAccess.addGame(testGame2);

        gameDataAccess.clear();
        authDataAccess.clear();
        userDataAccess.clear();

        assertEquals(0, userDataAccess.getNumUsers());
        assertEquals(0, authDataAccess.getNumAuths());
        assertEquals(0, gameDataAccess.getNumGames());
    }

}
