package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAOSQL;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        validateAuth(command.getAuthToken());
        //add session to connection map

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
        System.out.printf("move message received from %s%n", command.getUsername());
    }

    public void leave(UserGameCommand command){
        System.out.printf("leave message received from %s%n", command.getUsername());
    }

    public void resign(UserGameCommand command){
        System.out.printf("resign message received from %s%n", command.getUsername());
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
}
