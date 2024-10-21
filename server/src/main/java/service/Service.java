package service;

import dataaccess.AuthDAOMemory;
import dataaccess.UserDAOMemory;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();
    AuthDAOMemory authDAO = new AuthDAOMemory();

    public AuthData registerUser(UserData newUser) throws ServiceException {

        if( userDAO.getUser(newUser.username()) != null ){
            throw new ServiceException("user already exists");
        }
        userDAO.addUser(newUser);

        return new AuthData(generateAuthToken(), newUser.username());
    }


    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
