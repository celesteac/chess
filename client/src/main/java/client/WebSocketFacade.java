package client;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ui.EscapeSequences;
import ui.ServerMessageObserver;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationServerMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketFacade extends Endpoint {
    ServerMessageObserver messageObserver;
    Session session;
    ClientGameplay client;

    public WebSocketFacade(String url, ServerMessageObserver messageObserver, ClientGameplay client){
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageObserver = messageObserver;

            this.client = client;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
                    ServerMessage serverMessage;

                    switch (ServerMessage.ServerMessageType.valueOf(jsonObject.get("serverMessageType").getAsString())) {
                        case NOTIFICATION -> {
                            serverMessage = gson.fromJson(message, NotificationServerMessage.class);
                            onNotification((NotificationServerMessage) serverMessage);
                        }
                        case ERROR -> {
                            serverMessage = gson.fromJson(message, ErrorServerMessage.class);
                            onError((ErrorServerMessage) serverMessage);
                        }
                        case LOAD_GAME -> {
                            serverMessage = gson.fromJson(message, LoadGameMessage.class);
                            onLoadGame((LoadGameMessage) serverMessage);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    /// SERVER MESSAGE FUNCTIONS ///

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    private void onNotification(NotificationServerMessage notification){
        messageObserver.notify(EscapeSequences.SET_TEXT_COLOR_BLUE + notification.getMessage());
    }

    private void onError(ErrorServerMessage errorMessage){
        messageObserver.notify(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getMessage());
    }

    private void onLoadGame(LoadGameMessage loadGameMessage){
        ChessBoard newBoard = loadGameMessage.getBoard();
        client.setBoard(newBoard);
        client.drawBoard();
    }

    /// CALLABLE FUNCTIONS

    public void connect(UserGameCommand connectCommand) throws ResponseException{
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(UserGameCommand leaveCommand){
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(MakeMoveCommand moveCommand){
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(UserGameCommand resignCommand){
        try{
            this.session.getBasicRemote().sendText(new Gson().toJson(resignCommand));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    /// HELPER FUNCTIONS //////

    private ServerMessage.ServerMessageType type(ServerMessage.ServerMessageType type) {
        return type;
    }

    private UserGameCommand.CommandType type(UserGameCommand.CommandType type) {
        return type;
    }
}
