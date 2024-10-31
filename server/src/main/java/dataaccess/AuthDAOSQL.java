package dataaccess;

import model.AuthData;

public class AuthDAOSQL implements AuthDAO {
    public void addAuthData(AuthData authData) {

    }

    public AuthData getAuthData(String authToken) {
        return null;
    }

    public boolean deleteAuth(AuthData auth) {
        return false;
    }

    public void clear() {

    }

    public int getNumAuths() {
        return 0;
    }
}
