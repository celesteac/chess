package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAOSQL;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationServerMessage;
import websocket.messages.ServerMessage;

import javax.imageio.IIOException;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            validateAuth(command.getAuthToken());
            //add session to connection map

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> move(session, (MakeMoveCommand) command);
                case LEAVE -> leave(session, command);
                case RESIGN -> resign(session, command);
            }
        } catch (IOException ex){
            System.out.printf("error, io exception %s%n", ex.getMessage()); //fixme
        }
    }

    public void connect(Session session, UserGameCommand command) throws IOException {
        System.out.printf("connect message received from %s%n", command.getUsername());
//        String message = "Connected!";
//        NotificationServerMessage notification = new NotificationServerMessage(type(NOTIFICATION), message);
//        String jsonMessage = new Gson().toJson(notification);
//        session.getRemote().sendString(jsonMessage);
    }

    public void move(Session session, MakeMoveCommand command) throws IOException {
        System.out.printf("move message received from %s%n", command.getUsername());
    }

    public void leave(Session session, UserGameCommand command) throws IOException {
        System.out.printf("leave message received from %s%n", command.getUsername());
    }

    public void resign(Session session, UserGameCommand command) throws IOException {
        System.out.printf("resign message received from %s%n", command.getUsername());
        String message = "From server: You resigned!";
        NotificationServerMessage notification = new NotificationServerMessage(type(NOTIFICATION), message);
        String jsonMessage = new Gson().toJson(notification);
        session.getRemote().sendString(jsonMessage);
    }



    /// HELPER FUNCTIONS /////

    private void validateAuth(String authtoken){
        try {
            AuthDAOSQL authDAO = new AuthDAOSQL();
            if(authDAO.getAuthData(authtoken) == null){
                System.out.println("Error: unauthorized"); //fixme
            }
        } catch (DataAccessException ex){
            System.out.printf("Error: %s%n", ex.getMessage()); //fixme
        }
    }

    private ServerMessage.ServerMessageType type(ServerMessage.ServerMessageType type) {
        return type;
    }

    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
