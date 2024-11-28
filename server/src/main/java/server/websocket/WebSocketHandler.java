package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command);
            case MAKE_MOVE -> move(command);
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
        }
    }

    public void connect(UserGameCommand command){
        System.out.printf("connect message received from %s%n", command.getUsername());
    }

    public void move(UserGameCommand command){
        System.out.println("move message received");
    }

    public void leave(UserGameCommand command){
        System.out.println("leave message received");
    }

    public void resign(UserGameCommand command){
        System.out.println("resign message received");
    }
}
