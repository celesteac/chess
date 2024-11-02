package dataaccess;

import model.AuthData;
import service.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthDAOSQL implements AuthDAO  {

    public void addAuthData(AuthData authData) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){

        }catch (Exception ex){
//            System.out.println("do something about this exception");
            throw new DataAccessException(ex.getMessage());
        }
    }

    public AuthData getAuthData(String authToken) throws DataAccessException{
        return null;
    }

    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        return false;
    }

    public void clear() throws DataAccessException {

    }

    public int getNumAuths() throws DataAccessException {
        return 0;
    }
}
