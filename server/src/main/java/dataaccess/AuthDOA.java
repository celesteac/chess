package dataaccess;

import model.AuthData;

public interface AuthDOA extends DAO {
    public void addAuthData(AuthData authData);

    public AuthData getAuthData(String authToken);

    public boolean deleteAuth(AuthData auth);

    public void clear();

    public int getNumAuths();
}
