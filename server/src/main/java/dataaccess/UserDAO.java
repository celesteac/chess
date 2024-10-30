package dataaccess;

import model.UserData;

public interface UserDAO extends  DAO{
    public UserData getUser(String username);

    public void addUser(UserData newUser);

    public void clear();

    public int getNumUsers();

}
