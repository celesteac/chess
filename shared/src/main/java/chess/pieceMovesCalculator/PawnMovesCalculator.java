package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    public PawnMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        System.out.println("pawn");
        return this.legalMoves;
    }
}
