package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import websocket.messages.LoadGameMessage;
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
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            UserGameCommand command;

            switch (UserGameCommand.CommandType.valueOf(jsonObject.get("commandType").getAsString())) {
                case CONNECT -> {
                    command = gson.fromJson(message, UserGameCommand.class);
                    validateAuth(command.getAuthToken());
                    connect(session, command);
                }
                case MAKE_MOVE -> {
                    command = gson.fromJson(message, MakeMoveCommand.class);
                    validateAuth(command.getAuthToken());
                    move(session, (MakeMoveCommand) command);
                }
                case LEAVE -> {
                    command = gson.fromJson(message, UserGameCommand.class);
                    validateAuth(command.getAuthToken());
                    leave(session, command);
                }
                case RESIGN -> {
                    command = gson.fromJson(message, UserGameCommand.class);
                    validateAuth(command.getAuthToken());
                    resign(session, command);
                }
            }
        } catch (IOException | DataAccessException | UnauthorizedWebSocketException | InvalidMoveException ex){
            sendErrorMessage(session, ex.getMessage());
        }
    }

    public void connect(Session session, UserGameCommand command) throws IOException, DataAccessException {
        String username = command.getUsername();
        int gameID = command.getGameID();

        System.out.printf("connect message received from %s%n", username);
        saveSession(command, session);

        ChessBoard board = getGameBoard(command.getGameID());
        sendLoadBoardSingle(session, board);
        sendNotificationSingle(session, "Current board");

        String message = createConnectMessage(getPlayerColor(gameID, username), username);
        sendNotification(message, gameID, username);
    }

    public void move(Session session, MakeMoveCommand command) throws IOException, DataAccessException, InvalidMoveException {
        System.out.printf("move message received from %s%n", command.getUsername());

        //Access game from database
        int gameID = command.getGameID();
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        //checks game not resigned
        //checks legal player (not observer)
        //make move
        ChessMove move = command.getMove();
        game.makeMove(move);
            //game checks legal turn and legal move?
        //store game in database
        GameData updatedGame = gameData.updateGame(game);
        gameDAO.updateGame(updatedGame);
        //send load game to all clients
        sendLoadBoardAll(game.getBoard(), gameID);
        //send notification to all other clients
        String username = command.getUsername();
        String message = username + " moved from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
        sendNotification(message, gameID, username);

//        String message = "Moving from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
//        sendNotificationSingle(session, message);
    }

    public void leave(Session session, UserGameCommand command) throws IOException, DataAccessException {
        String username = command.getUsername();
        int gameID = command.getGameID();

        removePlayerFromGameDB(username, gameID);

        ConnectionManager connectionManager = connectionMap.get(command.getGameID());
        connectionManager.removeConnection(command.getUsername());

        sendNotification(username + " left the game", gameID, username);
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

    private void sendLoadBoardAll(ChessBoard board, int gameID) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(type(LOAD_GAME), board);
        String jsonMessage = new Gson().toJson(loadGameMessage, LoadGameMessage.class);
        ConnectionManager connectionManager = connectionMap.get(gameID);
        connectionManager.broadcast(null, jsonMessage);
    }

    private void sendLoadBoardSingle(Session session, ChessBoard board) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(type(LOAD_GAME), board);
        String jsonMessage = new Gson().toJson(loadGameMessage, LoadGameMessage.class);
        session.getRemote().sendString(jsonMessage);
    }

    private void sendNotificationSingle(Session session, String message) throws IOException {
        NotificationServerMessage notification = new NotificationServerMessage(type(NOTIFICATION), message);
        String jsonMessage = new Gson().toJson(notification);
        session.getRemote().sendString(jsonMessage);
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
            return username+ " connected as an observer";
        }
    }

    private ChessGame.TeamColor getPlayerColor(int gameID, String username) throws DataAccessException {
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData game = gameDAO.getGame(gameID);
        return game.getPlayerColor(username);
    }

    private void removePlayerFromGameDB(String username, int gameID) throws DataAccessException{
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData game = gameDAO.getGame(gameID);
        GameData updatedGame = game.updateGamePlayer(getPlayerColor(gameID, username), null);
        gameDAO.updateGame(updatedGame);
    }

    private ChessBoard getGameBoard(int gameID) throws DataAccessException {
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData gameData = gameDAO.getGame(gameID);
        return gameData.game().getBoard();
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
