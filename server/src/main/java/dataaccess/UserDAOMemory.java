package dataaccess;

import model.UserData;

import java.util.*;

public class UserDAOMemory implements UserDAO {
    Map<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }
//        for(UserData user : users){
//            if(Objects.equals(user.username(), username)){
//                return user; //return user
//            }
//        }
//        return null;
//        }
//    }

    public void addUser(UserData newUser){
        users.put(newUser.username(), newUser);
    }
}
