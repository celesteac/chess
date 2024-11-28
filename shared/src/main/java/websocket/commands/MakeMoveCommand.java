package websocket.commands;

import chess.ChessBoard;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, String username, Integer gameID, ChessMove move) {
        super(commandType, authToken, username, gameID);
        this.move = move;
    }
}
