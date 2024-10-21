package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

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


}
