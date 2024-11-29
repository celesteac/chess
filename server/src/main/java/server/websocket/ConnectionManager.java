package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    int gameID;

    public ConnectionManager(int gameID){
        this.gameID = gameID;
    }

    public void addConnection(Session session, String username){
        if(username == null){
            username = "null";
        }
        connections.put(username, new Connection(session, username));
    }

    public void removeConnection(String username){
        if(username == null){
            username = "null";
        }
        connections.remove(username);
    }

    public void broadcast(String excludePlayerName, String message) throws IOException {
        if(excludePlayerName == null){
            excludePlayerName = "null";
        }

        var removeList = new ArrayList<Connection>();

        for (var conn : connections.values()) {
            if (conn.session.isOpen()) {
                if (!conn.username.equals(excludePlayerName)) {
                    conn.send(message);
                }
            } else {
                removeList.add(conn);
            }
        }

        // Clean up any connections that were left open.
        for (var conn : removeList) {
            connections.remove(conn.username);
        }

    }
}
