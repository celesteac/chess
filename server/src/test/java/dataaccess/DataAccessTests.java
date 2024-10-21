package dataaccess;


import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import service.Service;

public class DataAccessTests {

    @Test
    void addAndGetUser(){
        UserDAOMemory dataaccess = new UserDAOMemory();
        UserData expected = new UserData(
                "susy","yum", "maily"
        );
        dataaccess.addUser(expected);
        UserData actual = dataaccess.getUser("susy");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addAndGetAuthData(){
        Service service = new Service();
        AuthDAOMemory dataaccess = new AuthDAOMemory();
        AuthData expected = new AuthData(
                service.generateAuthToken(), "ellie"
        );
        dataaccess.addAuthData(expected);
        AuthData actual = dataaccess.getAuthData(expected.authToken());
        System.out.println(expected.authToken());
        Assertions.assertEquals(expected,actual);
    }

}
