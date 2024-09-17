package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;

public abstract class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition position;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract ArrayList<ChessMove> calculateLegalMoves();

}
