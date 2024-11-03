package server;

public record CreateRequest(String gameName, String authToken) {

    CreateRequest addAuthToken(String authToken){
        return new CreateRequest(gameName, authToken);
    }
}
