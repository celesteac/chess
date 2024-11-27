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
            case CONNECT -> connect();
            case MAKE_MOVE -> move();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    public void connect(){
        System.out.println("connect message received");
    }

    public void move(){
        System.out.println("move message received");
    }

    public void leave(){
        System.out.println("leave message received");
    }

    public void resign(){
        System.out.println("resign message received");
    }
}
