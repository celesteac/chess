package service;

import dataaccess.UserDAOMemory;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.UUID;

public class ServiceTest {

//    @BeforeAll

    @Test
    void registerUser(){
        // right now
        // give it a UserData object
        // get it back
        // or get back null if it already exists
    }

    @Test
    void getNewAuthToken(){
        Service s = new Service();
        String uuid = s.generateAuthToken();
        System.out.println(uuid);
        Assertions.assertEquals(36, uuid.length());
    }

}
