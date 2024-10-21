package service;

import dataaccess.AuthDAOMemory;
import dataaccess.GameDAOMemory;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();
    AuthDAOMemory authDAO = new AuthDAOMemory();
    GameDAOMemory gameDAOMemory = new GameDAOMemory();

    public AuthData registerUser(UserData newUser) throws ServiceException {

        if( userDAO.getUser(newUser.username()) != null ){
            throw new ServiceException("user already exists");
        }
        userDAO.addUser(newUser);
        AuthData auth = new AuthData(generateAuthToken(), newUser.username());
        authDAO.addAuthData(auth);
        //test this?

        return auth;
    }

    public void clearDB(){
        //clear each of the thingies
        //is this void
    }


    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
