package client;


import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    String serverUrl;

    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    void login(String username, String password){
        String path = "/session";

    }

    AuthData register(String username, String password, String email) throws ResponseException{
        String path = "/user";
        UserData newUser = new UserData(username, password, email);
        AuthData auth =  makeRequest("POST", path, newUser, AuthData.class);
        return auth;
    }

    void logout(){
        String path = "/session";
    }

    void createGame(String gameName){
        String path = "/game";

    }

    void listGames(){
        String path = "/game";

    }

    void joinGame(){
        String path = "/game";

    }

    void delete(){
        String path = "/db";
        makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            String absoluteURL = serverUrl + path;
            URL url = (new URI(absoluteURL)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
