package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        return this.legalMoves;
    }
}
