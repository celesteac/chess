package client;

import com.google.gson.Gson;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
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

    public WebSocketFacade(String url, ServerMessageObserver messageObserver){
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageObserver = messageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> onNotification(serverMessage);
                        case ERROR -> onError(serverMessage);
                        case LOAD_GAME -> onLoadGame(serverMessage);
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

    private void onNotification(ServerMessage serverMessage){

    }

    private void onError(ServerMessage serverMessage){

    }

    private void onLoadGame(ServerMessage serverMessage){

    }

    /// CALLABLE FUNCTIONS

    public void connect() throws ResponseException{
        try{
            UserGameCommand command = new UserGameCommand(type(CONNECT), "authtoken", 1234); //FIXME
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage()); //is this the right error code?
        }
    }

    public void leave(){
        try{
            UserGameCommand command = new UserGameCommand(type(LEAVE), "authtoken", 1234); //FIXME
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage()); //is this the right error code?
        }
    }

    public void makeMove(){
        try{
            UserGameCommand command = new UserGameCommand(type(MAKE_MOVE), "authtoken", 1234); //FIXME
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage()); //is this the right error code?
        }
    }

    public void resign(){
        try{
            UserGameCommand command = new UserGameCommand(type(RESIGN), "authtoken", 1234); //FIXME
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(500, ex.getMessage()); //is this the right error code?
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
