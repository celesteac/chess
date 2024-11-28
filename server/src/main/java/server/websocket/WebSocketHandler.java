package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAOSQL;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameDAOSQL;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.UnauthorizedWebSocketException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorServerMessage;
import websocket.messages.NotificationServerMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    Map<Integer, ConnectionManager> connectionMap = new HashMap<>();

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
        } catch (IOException | DataAccessException | UnauthorizedWebSocketException ex){
            sendErrorMessage(session, ex.getMessage());
        }
    }

    public void connect(Session session, UserGameCommand command) throws IOException, DataAccessException {
        String username = command.getUsername();
        int gameID = command.getGameID();

        System.out.printf("connect message received from %s%n", username);
        saveSession(command, session);
        //send load game to recently connected session

        String message = createConnectMessage(getPlayerColor(gameID, username), username);
        sendNotification(message, gameID, username);
    }

    public void move(Session session, MakeMoveCommand command) throws IOException {
        System.out.printf("move message received from %s%n", command.getUsername());
    }

    public void leave(Session session, UserGameCommand command) throws IOException {
        String username = command.getUsername();
        int gameID = command.getGameID();
        //remove player from game in database
        //remove from connection manager
        ConnectionManager connectionManager = connectionMap.get(command.getGameID());
        connectionManager.removeConnection(command.getUsername());
        //send notification to other players
        String message = username + " left the game";
        sendNotification(message, gameID, username);
        //close ws connection?
        //client moves to logged in state
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

    private ChessGame.TeamColor getPlayerColor(int gameID, String username) throws DataAccessException {
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData game = gameDAO.getGame(gameID);
        return game.getPlayerColor(username);
    }

    private void sendNotification(String message, int gameID, String excludePlayerName) throws IOException {
        NotificationServerMessage notification = new NotificationServerMessage(type(NOTIFICATION), message);
        String jsonMessage = new Gson().toJson(notification);
        ConnectionManager connectionManager = connectionMap.get(gameID);
        connectionManager.broadcast(excludePlayerName, jsonMessage);
    }

    private void sendErrorMessage(Session session, String message) {
        try {
            ErrorServerMessage errorMessage = new ErrorServerMessage(type(ERROR), message);
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
        } catch (IOException ex){
            System.out.println("IO Exception when sending error message");
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private String createConnectMessage(ChessGame.TeamColor playerColor, String username){
        if(playerColor == ChessGame.TeamColor.WHITE){
            return username + " connected as white";
        } else if (playerColor == ChessGame.TeamColor.BLACK){
            return username + " connected as black";
        } else {
            return username+ " is observing";
        }
    }


    private void saveSession(UserGameCommand command, Session session){
        int gameID = command.getGameID();
        if(connectionMap.get(gameID) == null){
            connectionMap.put(gameID, new ConnectionManager(gameID));
        }
        ConnectionManager connectionManager = connectionMap.get(gameID);
        connectionManager.addConnection(session, command.getUsername());
    }


    private void validateAuth(String authtoken) throws UnauthorizedWebSocketException, DataAccessException{
            AuthDAOSQL authDAO = new AuthDAOSQL();
            if(authDAO.getAuthData(authtoken) == null){
                throw new UnauthorizedWebSocketException("Error: unauthorized");
            }
    }

    private ServerMessage.ServerMessageType type(ServerMessage.ServerMessageType type) {
        return type;
    }

    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
