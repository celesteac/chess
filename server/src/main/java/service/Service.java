package service;

import dataaccess.UserDAOMemory;
import model.UserData;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();

    public UserData registerUser(UserData newUser) throws ServiceException {
        //will check DAO for a user by that name
        //if no error, create a userData object and send it to DAO
        //create authToken and AuthData object, send to DAO
        //return register result

        if( userDAO.getUser(newUser.username()) != null ){
            throw new ServiceException("user already exists");
        }

        userDAO.addUser(newUser);
        return newUser;

    }

    public String registerUserHardcode(String user){
        return user + " yay";
    }
}
