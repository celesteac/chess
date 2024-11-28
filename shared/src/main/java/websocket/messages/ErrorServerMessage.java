package websocket.messages;

public class ErrorServerMessage extends ServerMessage {
    String message;

    public ErrorServerMessage(ServerMessageType type, String message){
        super(type);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
