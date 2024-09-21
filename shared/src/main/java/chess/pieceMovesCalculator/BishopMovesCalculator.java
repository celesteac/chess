package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        System.out.println("bishop");
        return this.legalMoves;
    }
}
