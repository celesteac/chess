package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public ArrayList<ChessMove> calculateLegalMoves(){
        return new ArrayList<ChessMove>();
    }
}
