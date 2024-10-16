package service;

import model.UserData;

public class Service {
    public UserData registerUser(UserData newUser){
        //will check DAO for a user by that name
        //if no error, create a userData object and send it to DAO
        //create authToken and AuthData obejct, send to DAO
        //return register result
        return newUser;
    }

    public String registerUserHardcode(String user){
        return user + " yay";
    }
}
