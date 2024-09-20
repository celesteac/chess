package chess.pieceMovesCalculator;

import chess.ChessBoard;
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

}
