package dataaccess;

import model.AuthData;

public interface AuthDAO extends DAO {
    public void addAuthData(AuthData authData) throws DataAccessException;

    public AuthData getAuthData(String authToken) throws DataAccessException;

    public boolean deleteAuth(AuthData auth) throws DataAccessException ;

    public void clear() throws DataAccessException;

    public int getNumAuths() throws DataAccessException;
}
