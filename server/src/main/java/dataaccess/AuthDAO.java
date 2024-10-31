package dataaccess;

import model.AuthData;

public interface AuthDAO extends DAO {
    public void addAuthData(AuthData authData);

    public AuthData getAuthData(String authToken);

    public boolean deleteAuth(AuthData auth);

    public void clear();

    public int getNumAuths();
}
