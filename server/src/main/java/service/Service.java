package service;

import dataaccess.AuthDAOMemory;
import dataaccess.GameDAOMemory;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.UUID;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();
    AuthDAOMemory authDAO = new AuthDAOMemory();
    GameDAOMemory gameDAO = new GameDAOMemory();

    public AuthData registerUser(UserData newUser) throws ServiceException {

        if(!checkValidRegisterRequest(newUser)) {
            throw new ServiceException("Error: bad request", 400);
        }
        if( userDAO.getUser(newUser.username()) != null ){
            throw new ServiceException("Error: user already exists", 403);
        }

        userDAO.addUser(newUser);
        AuthData auth = new AuthData(generateAuthToken(), newUser.username());
        authDAO.addAuthData(auth);
        //test this?

        return auth;
    }

    public AuthData login(UserData user) throws ServiceException{
        if (userDAO.getUser(user.username()) == null){
            throw new  ServiceException("Error: unauthorized", 401);
        }

        String authToken = generateAuthToken();
        return new AuthData(authToken, user.username());
    }



    public void clearDB() throws ServiceException{
        //clear each of the thingies
        //is this void
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        if(gameDAO.getNumGames() > 0 || authDAO.getNumAuths() > 0 || userDAO.getNumUsers() >0){
            throw new ServiceException("Error: Database failed to clear", 500);
        }
    }



    //make this an internal class??
    private boolean checkValidRegisterRequest(UserData user) {
        return (user.username() != null
                && user.password() != null
                && user.email() != null);
    }

    //not this one
    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
