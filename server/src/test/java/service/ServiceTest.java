package service;

import dataaccess.GameDAO;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.UUID;

public class ServiceTest {
    private static Service service;
    private static UserData testUser;
    private static GameData testGame;
    private static AuthData testAuth;

    @BeforeAll
    public static void init(){
        service = new Service();
        testUser = new UserData("miguel", "cute", "the cutest");
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

}
