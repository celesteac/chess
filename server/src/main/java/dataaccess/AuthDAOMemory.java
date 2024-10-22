package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAOMemory implements AuthDOA {
    Map<String, AuthData> authDataStorage = new HashMap<>();

    public void addAuthData(AuthData authData){
        authDataStorage.put(authData.authToken(), authData);
    }

    public AuthData getAuthData(String authToken){
        return authDataStorage.get(authToken);
    }

    public boolean deleteAuth(AuthData auth){
        return authDataStorage.remove(auth.authToken(), auth);
    }

    public void clear(){
        authDataStorage.clear();
    }

    public int getNumAuths(){
        return authDataStorage.size();
    }
}
