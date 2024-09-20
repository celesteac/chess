package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition position;
    Collection<ChessMove> legalMoves = new ArrayList<>();

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.position = position;
        this.board = board;
    }

    public abstract Collection<ChessMove> calculateLegalMoves();


    public boolean checkBounds(int row, int col) {
        return (row > 0 && row < 9 && col > 0 && col < 9);
    }
    public boolean checkEmpty(ChessPosition testPosition) {
        return board.getPiece(testPosition) == null;
    }
    public boolean checkEnemy(ChessPosition testPosition, ChessGame.TeamColor color) {
        return !board.getPiece(position).getTeamColor().equals(color);
    }
}
