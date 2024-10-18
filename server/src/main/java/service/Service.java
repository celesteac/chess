package service;

import dataaccess.UserDAOMemory;
import model.UserData;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();

    public UserData registerUser(UserData newUser){
        //will check DAO for a user by that name
        //if no error, create a userData object and send it to DAO
        //create authToken and AuthData object, send to DAO
        //return register result

        if( userDAO.getUser(newUser.username()) == null ){
            userDAO.addUser(newUser);
            return newUser;
        }
        else{
            //throw error? return HTTP thingy
            //how? data access exception? where do I catch it?
            return null;
        }

    }

    public String registerUserHardcode(String user){
        return user + " yay";
    }
}
