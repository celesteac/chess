package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    Session session;
    String username;
//    ChessGame.TeamColor playerColor;

    public Connection(Session session, String username){ //, ChessGame.TeamColor playerColor
        this.session = session;
        this.username = username;
//        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
