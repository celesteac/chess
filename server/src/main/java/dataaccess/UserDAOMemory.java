package dataaccess;

import model.UserData;

import java.util.*;

public class UserDAOMemory implements UserDAO {
    Map<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void addUser(UserData newUser){
        users.put(newUser.username(), newUser);
    }
}
