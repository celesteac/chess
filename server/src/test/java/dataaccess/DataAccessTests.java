package dataaccess;


import model.AuthData;
import model.UserData;
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
    void addDuplicateAuth(){
        //later
    }

}
