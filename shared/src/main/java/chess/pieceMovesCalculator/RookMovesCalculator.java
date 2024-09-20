package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        return this.legalMoves;
    }
}

