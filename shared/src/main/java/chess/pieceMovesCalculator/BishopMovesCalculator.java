package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        return this.legalMoves;
    }
}
