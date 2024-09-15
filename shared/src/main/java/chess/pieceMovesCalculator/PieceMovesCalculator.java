package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    Collection<ChessMove> legalMoves;
    ChessBoard board;
    ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        calculateLegalMoves();
    }

    private void calculateLegalMoves() {
        legalMoves = new ArrayList<ChessMove>();
    }

    public Collection<ChessMove> getLegalMoves() {
        return legalMoves;
    }

}
