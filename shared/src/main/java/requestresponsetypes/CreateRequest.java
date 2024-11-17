package requestresponsetypes;

public record CreateRequest(String gameName, String authToken) {

    public CreateRequest addAuthToken(String authToken){
        return new CreateRequest(gameName, authToken);
    }
}
