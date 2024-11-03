package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.DatabaseManager;
import service.Service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DataAccessSQLTests {
    private static Service service;
    private static UserData testUser1;
    private static UserData testUser2;
    private static AuthData testAuth1;
    private static AuthData testAuth2;
    private static GameData testGame1;
    private static GameData testGame2;
    private static final UserDAOSQL USER_DATA_ACCESS = new UserDAOSQL();
    private static final AuthDAOSQL AUTH_DATA_ACCESS = new AuthDAOSQL();
    private static final GameDAOSQL GAME_DATA_ACCESS = new GameDAOSQL();

    @BeforeAll
    public static void init(){
        service = new Service();
        testUser1 = new UserData("elli", "balloons", "waterfalls@mail");
        testUser2 = new UserData("russel", "dogs", "birds@mail");
        testAuth1 = new AuthData(service.generateAuthToken(), "carl");
        testAuth2 = new AuthData(service.generateAuthToken(), "Kevin" );
        testGame1 = new GameData(
                new ChessGame(), null, null,
                "coolest game", 1234
        );
        testGame2 = new GameData(
                new ChessGame(), null, null,
                "adventure", 5678
        );
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        USER_DATA_ACCESS.clear();
        AUTH_DATA_ACCESS.clear();
        GAME_DATA_ACCESS.clear();
    }

    @Test
    public void databaseCreation() throws DataAccessException{
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
        try (Connection conn = DatabaseManager.getConnection()){
            String statement = "SELECT COUNT(*) AS table_count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'chess'";
            try(var preparedStatement = conn.prepareStatement(statement)){
                try (var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        int count = rs.getInt("table_count");
                        assertEquals(3, count);
                    } else {
                        fail("no results returned from the query");
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    /// AUTHDAO TESTS ///

    @Test
    void addAndGetAuthData() throws DataAccessException {
        AuthData expected = testAuth1;

        AUTH_DATA_ACCESS.addAuthData(testAuth2);
        AUTH_DATA_ACCESS.addAuthData(expected);
        AuthData actual = AUTH_DATA_ACCESS.getAuthData(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    public void getNumAuths() throws DataAccessException {
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        AUTH_DATA_ACCESS.addAuthData(testAuth2);
        int numAuthsFound = AUTH_DATA_ACCESS.getNumAuths();
        assertEquals(2, numAuthsFound);
    }

    @Test
    public void clearAuth() throws DataAccessException{
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        AUTH_DATA_ACCESS.addAuthData(testAuth2);
        AUTH_DATA_ACCESS.clear();
        int numAuthsFound = AUTH_DATA_ACCESS.getNumAuths();
        assertEquals(0, numAuthsFound);
    }

    @Test
    public void deleteAuth() throws DataAccessException {
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        AUTH_DATA_ACCESS.addAuthData(testAuth2);
        AUTH_DATA_ACCESS.deleteAuth(testAuth1);
        assertNull(AUTH_DATA_ACCESS.getAuthData(testAuth1.authToken()));
    }

    /// USERDAO TESTS ////

    @Test
    public void getNumUsers() throws DataAccessException {
        USER_DATA_ACCESS.addUser(testUser1);
        USER_DATA_ACCESS.addUser(testUser2);
        int numUsersFound = USER_DATA_ACCESS.getNumUsers();
        assertEquals(2, numUsersFound);
    }

    @Test
    public void clearUsers() throws DataAccessException{
        USER_DATA_ACCESS.addUser(testUser1);
        USER_DATA_ACCESS.addUser(testUser2);
        USER_DATA_ACCESS.clear();
        int numUsersFound = USER_DATA_ACCESS.getNumUsers();
        assertEquals(0, numUsersFound);
    }


    @Test
    void addAndGetUser() throws DataAccessException{
        UserData expected = testUser1;
        USER_DATA_ACCESS.addUser(expected);
        USER_DATA_ACCESS.addUser(testUser2);
        UserData actual = USER_DATA_ACCESS.getUser(testUser1.username());
        assertEquals(expected, actual);
    }

    /// GAMEDAO TESTS ///

    @Test
    void addAndGetGamaData() throws DataAccessException{
        GameData expected = testGame1;
        GAME_DATA_ACCESS.addGame(expected);
        GAME_DATA_ACCESS.addGame(testGame2);
        GameData actual = GAME_DATA_ACCESS.getGame(expected.gameID());
        assertEquals(expected, actual);
    }

    @Test
    public void getNumGames() throws DataAccessException {
        GAME_DATA_ACCESS.addGame(testGame1);
        GAME_DATA_ACCESS.addGame(testGame2);
        int numGamesFound = GAME_DATA_ACCESS.getNumGames();
        assertEquals(2, numGamesFound);
    }

    @Test
    public void clearGames() throws DataAccessException{
        GAME_DATA_ACCESS.addGame(testGame1);
        GAME_DATA_ACCESS.addGame(testGame2);
        GAME_DATA_ACCESS.clear();
        int numGamesFound = GAME_DATA_ACCESS.getNumGames();
        assertEquals(0, numGamesFound);
    }

    @Test
    public void updateGame() throws DataAccessException{
        GameData testGame2Updated = testGame2.updateGamePlayer(ChessGame.TeamColor.WHITE, "kittens");
        GAME_DATA_ACCESS.addGame(testGame2);
        GAME_DATA_ACCESS.updateGame(testGame2Updated);
        GameData actual = GAME_DATA_ACCESS.getGame(testGame2Updated.gameID());
        System.out.println(actual);
        assertEquals(testGame2Updated, actual);
    }


    @Test
    void getAllGames() throws DataAccessException{
        GAME_DATA_ACCESS.addGame(testGame1);
        GAME_DATA_ACCESS.addGame(testGame2);
        Map<Integer, GameData> actual = GAME_DATA_ACCESS.getAllGames();

        Map<Integer, GameData> expected = new HashMap<>();
        expected.put(testGame1.gameID(), testGame1);
        expected.put(testGame2.gameID(), testGame2);

        assertEquals(expected, actual);
    }



    /// GENERAL TESTS ///

    @Test
    void clearDB() throws DataAccessException{
        USER_DATA_ACCESS.addUser(testUser1);
        USER_DATA_ACCESS.addUser(testUser2);
        AUTH_DATA_ACCESS.addAuthData(testAuth1);
        AUTH_DATA_ACCESS.addAuthData(testAuth2);
        GAME_DATA_ACCESS.addGame(testGame1);
        GAME_DATA_ACCESS.addGame(testGame2);

        GAME_DATA_ACCESS.clear();
        AUTH_DATA_ACCESS.clear();
        USER_DATA_ACCESS.clear();

        assertEquals(0, USER_DATA_ACCESS.getNumUsers());
        assertEquals(0, AUTH_DATA_ACCESS.getNumAuths());
        assertEquals(0, GAME_DATA_ACCESS.getNumGames());
    }

}
