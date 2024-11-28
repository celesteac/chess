package websocket.messages;

public class NotificationServerMessage extends ServerMessage {
    String message;

    public NotificationServerMessage(ServerMessageType type, String message){
        super(type);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
