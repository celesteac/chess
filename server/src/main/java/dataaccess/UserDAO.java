package dataaccess;

import model.UserData;

public interface UserDAO extends  DAO{
    public UserData getUser(String username) throws DataAccessException;

    public void addUser(UserData newUser) throws DataAccessException;

    public void clear() throws DataAccessException;

    public int getNumUsers() throws DataAccessException;

}
