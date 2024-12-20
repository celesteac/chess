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
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
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
                    String username = validateAuth(command.getAuthToken());
                    connect(session, command, username);
                }
                case MAKE_MOVE -> {
                    command = gson.fromJson(message, MakeMoveCommand.class);
                    String username = validateAuth(command.getAuthToken());
                    move(session, (MakeMoveCommand) command, username);
                }
                case LEAVE -> {
                    command = gson.fromJson(message, UserGameCommand.class);
                    String username = validateAuth(command.getAuthToken());
                    leave(session, command, username);
                }
                case RESIGN -> {
                    command = gson.fromJson(message, UserGameCommand.class);
                    String username = validateAuth(command.getAuthToken());
                    resign(session, command, username);
                }
            }
        } catch (IOException | DataAccessException | UnauthorizedWebSocketException | InvalidMoveException ex){
            sendErrorMessage(session, ex.getMessage());
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable ex){
        sendErrorMessage(session, ex.getMessage());
    }

    public void connect(Session session, UserGameCommand command, String username) throws IOException, DataAccessException {
        int gameID = command.getGameID();

        System.out.printf("connect message received from %s%n", username);
        saveSession(command, session, username);

        ChessGame game = getGame(command.getGameID());
        sendLoadBoardSingle(session, game);
//        sendNotificationSingle(session, "Current board");

        String message = createConnectMessage(getPlayerColor(gameID, username), username);
        sendNotification(message, gameID, username);
    }

    public void move(Session session, MakeMoveCommand command, String username) throws IOException, DataAccessException, InvalidMoveException {
        System.out.printf("move message received from %s%n", command.getUsername());

        //Access game from database
        int gameID = command.getGameID();
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        //checks game not resigned
        if(game.getIsResigned()){
            throw new UnauthorizedWebSocketException("Error: game is finished");
        }
        //checks legal player (not observer)
        ChessGame.TeamColor playerColor = getPlayerColor(gameID, username);
        if(playerColor == null){
            throw new UnauthorizedWebSocketException("Error: you are not a player");
        }
        //check if player turn
        if(playerColor != game.getTeamTurn()){
            throw new UnauthorizedWebSocketException("Error: it is not your turn");
        }
        //make move
        ChessMove move = command.getMove();
        game.makeMove(move);
        //game checks legal move
        //store game in database
        GameData updatedGame = gameData.updateGame(game);
        gameDAO.updateGame(updatedGame);
        //send load game to all clients
        sendLoadBoardAll(game, gameID);
        //send notification to all other clients
        String message = username + " moved from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
//        sendNotificationSingle(session, "");
        sendNotification(message, gameID, username);
        //send check or checkmate notifications
        String otherUsername = playerColor == ChessGame.TeamColor.WHITE ? gameData.blackUsername() : gameData.whiteUsername();
        checkGameState(session, username, otherUsername, gameID, game);

    }

    public void leave(Session session, UserGameCommand command, String username) throws IOException, DataAccessException {
        int gameID = command.getGameID();

        if(getPlayerColor(gameID, username) != null){
            removePlayerFromGameDB(username, gameID);
        }

        ConnectionManager connectionManager = connectionMap.get(command.getGameID());
        connectionManager.removeConnection(username);

        sendNotification(username + " left the game", gameID, username);
        System.out.printf("leave message received from %s%n", command.getUsername());

        session.close();
    }

    public void resign(Session session, UserGameCommand command, String username) throws IOException, DataAccessException {
        System.out.printf("resign message received from %s%n", command.getUsername());
        int gameID = command.getGameID();
            ChessGame game = getGame(gameID);
        if(getPlayerColor(gameID, username) == null){
            sendErrorMessage(session, "Error: you are observing");
        }
        else if(game.getIsResigned()){
            sendErrorMessage(session, "Error: game is already finished");
        }
        else {
            game.setResigned(true);
            storeUpdatedGame(gameID, game);

            sendNotificationSingle(session, "You resigned");
            sendNotification(username + " resigned", gameID, username);
        }
    }


    /// HELPER FUNCTIONS /////

    private void checkGameState(Session session, String activeUsername,  String otherUsername, int gameID, ChessGame game)
            throws DataAccessException, IOException{
        ChessGame.TeamColor playerColor = getPlayerColor(gameID, activeUsername);
        ChessGame.TeamColor otherPlayerColor = playerColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        if(game.isInCheckmate(playerColor)){
            game.setResigned(true);
            storeUpdatedGame(gameID, game);
            sendNotificationSingle(session, "You are in checkmate. Game is finished");
            sendNotification(activeUsername + " is in checkmate. Game is finished", gameID, activeUsername);
        }
        else if(game.isInCheckmate(otherPlayerColor)){
            game.setResigned(true);
            storeUpdatedGame(gameID, game);
            sendNotification(otherUsername + " is in checkmate. Game is finished", gameID, null);
        }
        else if(game.isInCheck(playerColor)){
            sendNotificationSingle(session, "You are in check");
            sendNotification(activeUsername + " is in check", gameID, activeUsername);
        }
        else if (game.isInCheck(otherPlayerColor)) {
            sendNotification(otherUsername + " is in check", gameID, null);
        }
        else if(game.isInStalemate(playerColor)){
            game.setResigned(true);
            storeUpdatedGame(gameID, game);
            sendNotificationSingle(session, "You are in stalemate. Game is finished");
            sendNotification(activeUsername + " is in stalemate. Game is finished", gameID, activeUsername);
        }
        else if(game.isInStalemate(otherPlayerColor)){
            game.setResigned(true);
            storeUpdatedGame(gameID, game);
            sendNotification(otherUsername + " is in stalemate. Game is finished", gameID, null);
        }
    }

    private void sendLoadBoardAll(ChessGame game, int gameID) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(type(LOAD_GAME), game);
        String jsonMessage = new Gson().toJson(loadGameMessage, LoadGameMessage.class);
        ConnectionManager connectionManager = connectionMap.get(gameID);
        connectionManager.broadcast(null, jsonMessage);
    }

    private void sendLoadBoardSingle(Session session, ChessGame game) throws IOException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(type(LOAD_GAME), game);
        String jsonMessage = new Gson().toJson(loadGameMessage);
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
            System.out.println("error message: " + message);
            ErrorServerMessage errorMessage = new ErrorServerMessage(type(ERROR), message);
            String jsonMessage = new Gson().toJson(errorMessage);
            session.getRemote().sendString(jsonMessage);
        } catch (IOException ex){
            System.out.println("IO Exception when sending error message");
            System.out.println("Original Error: " + message);
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
        if(getPlayerColor(gameID, username) == null){
            throw new DataAccessException("Server Error: trying to remove a observer from the database");
        }
        GameData updatedGame = game.updateGamePlayer(getPlayerColor(gameID, username), null);
        gameDAO.updateGame(updatedGame);
    }

    private ChessGame getGame(int gameID) throws DataAccessException {
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData gameData = gameDAO.getGame(gameID);
        if(gameData == null){
            throw new DataAccessException("Error: no game with game ID " + gameID);
        }
        return gameData.game();
    }

    private void storeUpdatedGame(int gameID, ChessGame game) throws DataAccessException{
        GameDAOSQL gameDAO = new GameDAOSQL();
        GameData gameData = gameDAO.getGame(gameID);

        GameData updatedGame = gameData.updateGame(game);
        gameDAO.updateGame(updatedGame);
    }

    private void saveSession(UserGameCommand command, Session session, String username){
        int gameID = command.getGameID();
        if(connectionMap.get(gameID) == null){
            connectionMap.put(gameID, new ConnectionManager(gameID));
        }
        ConnectionManager connectionManager = connectionMap.get(gameID);
        connectionManager.addConnection(session, username);
    }


    private String validateAuth(String authtoken) throws UnauthorizedWebSocketException, DataAccessException{
            AuthDAOSQL authDAO = new AuthDAOSQL();
            AuthData foundAuth = authDAO.getAuthData(authtoken);
            if(foundAuth == null){
                throw new UnauthorizedWebSocketException("Error: unauthorized");
            }
            return foundAuth.username();
    }

    private ServerMessage.ServerMessageType type(ServerMessage.ServerMessageType type) {
        return type;
    }

    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
