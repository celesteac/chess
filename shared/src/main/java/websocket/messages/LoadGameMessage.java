package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    ChessGame game;

    public LoadGameMessage(ServerMessageType type, ChessGame game){
        super(type);
        this.game = game;
    }

    public ChessGame getBoard(){
        return game;
    }
}
