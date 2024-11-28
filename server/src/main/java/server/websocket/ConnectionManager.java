package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    int gameID;

    public ConnectionManager(int gameID){
        this.gameID = gameID;
    }

    public void addConnection(Session session, String username){ // , ChessGame.TeamColor playerColor
        connections.put(username, new Connection(session, username)); // , playerColor
    }

    public void removeConnection(String username){
        connections.remove(username);
    }

    public void braodcast(String message) throws IOException {
        for(Connection conn : connections.values()){
            conn.send(message);
        }
    }
}
