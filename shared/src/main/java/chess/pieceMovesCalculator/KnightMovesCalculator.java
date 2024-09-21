package chess.pieceMovesCalculator;

import chess.*;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> calculateLegalMoves(){
        System.out.println("knight");
        return this.legalMoves;
    }
}
