package service;

import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.UUID;

public class ServiceTest {
    private static Service service;

    @BeforeAll
    public static void init(){
        service = new Service();
    }

    @Test
    void registerUser() throws ServiceException {
        // give it a UserData object
        // get back an AuthData with same username
        UserData user = new UserData("miguel", "cute", "the cutest");
        AuthData auth = service.registerUser(user);
        assertEquals(user.username(), auth.username());
        assertEquals(36, auth.authToken().length());
    }

    @Test
    void getNewAuthToken(){
//        Service s = new Service();
        String uuid = service.generateAuthToken();
        System.out.println(uuid);
        Assertions.assertEquals(36, uuid.length());
    }

}
