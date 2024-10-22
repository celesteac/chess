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

    @Test
    void addAndGetUser(){
        UserDAOMemory dataAccess = new UserDAOMemory();
        UserData expected = new UserData(
                "susy","yum", "maily"
        );
        dataAccess.addUser(expected);
        UserData actual = dataAccess.getUser("susy");
        assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        Service service = new Service();
        AuthDAOMemory dataAccess = new AuthDAOMemory();
        AuthData expected = new AuthData(
                service.generateAuthToken(), "ellie"
        );
        dataAccess.addAuthData(expected);
        AuthData actual = dataAccess.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        assertEquals(expected,actual);
    }

    @Disabled
    void addDuplicateAuthThrowsError(){
        //later
    }

    @Test
    void addAndGetGamaData(){
        GameDAOMemory gameDAO = new GameDAOMemory();
        GameData expected = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
        gameDAO.addGame(expected);
        GameData actual = gameDAO.getGame(expected.gameID());
        assertEquals(expected, actual);

    }

    @Test
    void clearDB(){
        UserData user = new UserData(
                "carl", "balloons", "cliff@mail"
        );
        AuthData auth = new AuthData(
                "123456", "carl"
        );
        GameData game = new GameData(
                new ChessGame(), null, null,
                "best game", 1234
        );
        UserDAOMemory userDAO = new UserDAOMemory();
        AuthDAOMemory authDAO = new AuthDAOMemory();
        GameDAOMemory gameDAO = new GameDAOMemory();
        userDAO.addUser(user);
        authDAO.addAuthData(auth);
        gameDAO.addGame(game);
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
        assertEquals(0, userDAO.getNumUsers());
        assertEquals(0, authDAO.getNumAuths());
        assertEquals(0, gameDAO.getNumGames());
    }

}
