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
    ChessGame.TeamColor color;
    Collection<ChessMove> legalMoves = new ArrayList<>();

    public PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.position = position;
        this.board = board;
        this.color = color;
    }

    public abstract Collection<ChessMove> calculateLegalMoves();


    public boolean checkBounds(int row, int col) {
        return (row > 0 && row < 9 && col > 0 && col < 9);
    }
    public boolean checkEmpty(ChessPosition testPosition) {
        return board.getPiece(testPosition) == null;
    }
    public boolean checkEnemy(ChessPosition testPosition) {
        return !board.getPiece(testPosition).getTeamColor().equals(color);
    }

    public void addMove(ChessPosition endPosition){
        legalMoves.add(new ChessMove(position, endPosition, null));
    }
}
