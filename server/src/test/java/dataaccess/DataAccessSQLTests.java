package dataaccess;


import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.DatabaseManager;
import service.Service;
import service.ServiceException;

import javax.xml.crypto.Data;
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
    private static final UserDAOSQL userDataAccess = new UserDAOSQL();
    private static final AuthDAOSQL authDataAccess = new AuthDAOSQL();
    private static final GameDAOSQL gameDataAccess = new GameDAOSQL();

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
    public void setup() throws DataAccessException {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
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

        authDataAccess.addAuthData(testAuth2);
        authDataAccess.addAuthData(expected);
        AuthData actual = authDataAccess.getAuthData(expected.authToken());
        assertEquals(expected,actual);
    }

    @Test
    public void getNumAuths() throws DataAccessException {
        authDataAccess.addAuthData(testAuth1);
        authDataAccess.addAuthData(testAuth2);
        int numAuthsFound = authDataAccess.getNumAuths();
        assertEquals(2, numAuthsFound);
    }

    @Test
    public void clearAuth() throws DataAccessException{
        authDataAccess.addAuthData(testAuth1);
        authDataAccess.addAuthData(testAuth2);
        authDataAccess.clear();
        int numAuthsFound = authDataAccess.getNumAuths();
        assertEquals(0, numAuthsFound);
    }

    @Test
    public void deleteAuth() throws DataAccessException {
        authDataAccess.addAuthData(testAuth1);
        authDataAccess.addAuthData(testAuth2);
        authDataAccess.deleteAuth(testAuth1);
        assertNull(authDataAccess.getAuthData(testAuth1.authToken()));
    }

    /// USERDAO TESTS ////

    @Test
    public void addUser() throws DataAccessException{
        userDataAccess.addUser(testUser1);
        userDataAccess.addUser(testUser2);
    }

    @Test
    public void getNumUsers() throws DataAccessException {
        userDataAccess.addUser(testUser1);
        userDataAccess.addUser(testUser2);
        int numUsersFound = userDataAccess.getNumUsers();
        assertEquals(2, numUsersFound);
    }

    @Test
    public void clearUsers() throws DataAccessException{
        userDataAccess.addUser(testUser1);
        userDataAccess.addUser(testUser2);
        userDataAccess.clear();
        int numUsersFound = userDataAccess.getNumUsers();
        assertEquals(0, numUsersFound);
    }


    @Test
    void addAndGetUser() throws DataAccessException{
        UserData expected = testUser1;
        userDataAccess.addUser(expected);
        userDataAccess.addUser(testUser2);
        UserData actual = userDataAccess.getUser(testUser1.username());
        assertEquals(expected, actual);
    }

//
//
//    @Test
//    void addAndGetGamaData(){
//        GameData expected = testGame1;
//        gameDataAccess.addGame(expected);
//        GameData actual = gameDataAccess.getGame(expected.gameID());
//        assertEquals(expected, actual);
//
//    }
//
//    @Test
//    void getAllGames(){
//        gameDataAccess.addGame(testGame1);
//        gameDataAccess.addGame(testGame2);
//        Map<Integer, GameData> actual = gameDataAccess.getAllGames();
//
//        Map<Integer, GameData> expected = new HashMap<>();
//        expected.put(testGame1.gameID(), testGame1);
//        expected.put(testGame2.gameID(), testGame2);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void deleteAuthData() throws DataAccessException{
//        authDataAccess.addAuthData(testAuth1);
//        authDataAccess.deleteAuth(testAuth1);
//        assertNull(authDataAccess.getAuthData(testAuth1.authToken()));
//    }
//
//    @Test
//    void updateGameRecord(){
//        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, "bob");
//        System.out.println(updatedGame);
//        //should do some equals method here
//    }
//
//    @Test
//    void updateGameDataAccessLayer(){
//        gameDataAccess.addGame(testGame1);
//        String expectedUserName = "bob";
//
//        //try this with the blackUsername too
//        GameData updatedGame = testGame1.updateGame(ChessGame.TeamColor.WHITE, expectedUserName);
//        gameDataAccess.updateGame(updatedGame);
//
//        String actualUserName = gameDataAccess.getGame(testGame1.gameID()).whiteUsername();
//        assertEquals(expectedUserName, actualUserName);
//    }
//
//
//    @Test
//    void clearDB() throws DataAccessException{
//        userDataAccess.addUser(testUser1);
//        userDataAccess.addUser(testUser2);
//        authDataAccess.addAuthData(testAuth1);
//        authDataAccess.addAuthData(testAuth2);
//        gameDataAccess.addGame(testGame1);
//        gameDataAccess.addGame(testGame2);
//
//        gameDataAccess.clear();
//        authDataAccess.clear();
//        userDataAccess.clear();
//
//        assertEquals(0, userDataAccess.getNumUsers());
//        assertEquals(0, authDataAccess.getNumAuths());
//        assertEquals(0, gameDataAccess.getNumGames());
//    }

}
