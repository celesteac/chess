package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.Service;

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
    private static final UserDAOMemory USER_DATA_ACCESS = new UserDAOMemory();
    private static final AuthDAOMemory AUTH_DATA_ACCESS = new AuthDAOMemory();
    private static final GameDAOMemory GAME_DATA_ACCESS = new GameDAOMemory();

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
        USER_DATA_ACCESS.clear();
        AUTH_DATA_ACCESS.clear();
        GAME_DATA_ACCESS.clear();
    }

    @Test
    void addAndGetUser(){
        UserData expected = testUser1;
        USER_DATA_ACCESS.addUser(expected);
        UserData actual = USER_DATA_ACCESS.getUser(testUser1.username());
        assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        AuthData expected = testAuth1;

        AUTH_DATA_ACCESS.addAuthData(expected);
        AuthData actual = AUTH_DATA_ACCESS.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    void addAndGetGamaData(){
        GameData expected = testGame1;
        GAME_DATA_ACCESS.addGame(expected);
        GameData actual = GAME_DATA_ACCESS.getGame(expected.gameID());
        assertEquals(expected, actual);

    }

    @Test
    void getAllGames(){
        GAME_DATA_ACCESS.addGame(testGame1);
        GAME_DATA_ACCESS.addGame(testGame2);
        Map<Integer, GameData> actual = GAME_DATA_ACCESS.getAllGames();

        Map<Integer, GameData> expected = new HashMap<>();
        expected.put(testGame1.gameID(), testGame1);
        expected.put(testGame2.gameID(), testGame2);

        assertEquals(expected, actual);
    }

    @Test
    void deleteAuthData(){
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        AUTH_DATA_ACCESS.deleteAuth(testAuth1);
        assertNull(AUTH_DATA_ACCESS.getAuthData(testAuth1.authToken()));
    }

    @Test
    void updateGameRecord(){
        GameData updatedGame = testGame1.updateGamePlayer(ChessGame.TeamColor.WHITE, "bob");
        System.out.println(updatedGame);
        //should do some equals method here
    }

    @Test
    void updateGameDataAccessLayer(){
        GAME_DATA_ACCESS.addGame(testGame1);
        String expectedUserName = "bob";

        GameData updatedGame = testGame1.updateGamePlayer(ChessGame.TeamColor.WHITE, expectedUserName);
        GAME_DATA_ACCESS.updateGame(updatedGame);

        String actualUserName = GAME_DATA_ACCESS.getGame(testGame1.gameID()).whiteUsername();
        assertEquals(expectedUserName, actualUserName);
    }


    @Test
    void clearDB(){
        USER_DATA_ACCESS.addUser(testUser1);
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        GAME_DATA_ACCESS.addGame(testGame1);

        GAME_DATA_ACCESS.clear();
        AUTH_DATA_ACCESS.clear();
        USER_DATA_ACCESS.clear();

        assertEquals(0, USER_DATA_ACCESS.getNumUsers());
        assertEquals(0, AUTH_DATA_ACCESS.getNumAuths());
        assertEquals(0, GAME_DATA_ACCESS.getNumGames());
    }

}
