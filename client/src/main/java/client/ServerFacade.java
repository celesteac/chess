package client;

import chess.ChessGame;
import requestresponsetypes.*;
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
import java.util.ArrayList;

public class ServerFacade {
    String serverUrl;

    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    AuthData login(String username, String password) throws ResponseException {
        String path = "/session";
        UserData newUser = new UserData(username, password, null);
        return makeRequest("POST", path, newUser, AuthData.class, null);
    }

    AuthData register(String username, String password, String email) throws ResponseException{
        String path = "/user";
        UserData newUser = new UserData(username, password, email);
        return makeRequest("POST", path, newUser, AuthData.class, null);
    }

    void logout(String authtoken){
        String path = "/session";
        makeRequest("DELETE", path, null, null, authtoken);
    }

    void createGame(String gameName, String authtoken) throws ResponseException {
        String path = "/game";
        CreateRequest createRequest = new CreateRequest(gameName);
        makeRequest("POST", path, createRequest, null, authtoken);
    }

    ArrayList<GameDetails> listGames(String authtoken) throws ResponseException {
        String path = "/game";
        ListResponse listResponse = makeRequest("GET", path, null, ListResponse.class, authtoken);
        return listResponse.games();
    }

    void joinGame(int gameID, ChessGame.TeamColor playColor, String authtoken) throws ResponseException {
        String path = "/game";
        JoinRequest joinReq = new JoinRequest(playColor, gameID, null);
        makeRequest("PUT", path, joinReq, null, authtoken);
    }

    void delete() throws ResponseException {
        String path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authtoken) throws ResponseException {
        try {
            String absoluteURL = serverUrl + path;
            URL url = (new URI(absoluteURL)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if(authtoken != null){
                http.setRequestProperty("Authorization", authtoken);
            }
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
//        String errorMessage = readBody(http, String.class);
        String errorMessage = http.getResponseMessage();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, errorMessage);
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
