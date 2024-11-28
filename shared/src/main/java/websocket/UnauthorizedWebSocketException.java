package websocket;

public class UnauthorizedWebSocketException extends RuntimeException {
    public UnauthorizedWebSocketException(String message) {
        super(message);
    }
}
