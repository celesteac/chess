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
    GameDAOMemory gameDAO = new GameDAOMemory();

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

    public void clearDB() throws ServiceException{
        //clear each of the thingies
        //is this void
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        if(gameDAO.getNumGames() > 0 || authDAO.getNumAuths() > 0 || userDAO.getNumUsers() >0){
            throw new ServiceException("Database failed to clear");
        }
    }

    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
