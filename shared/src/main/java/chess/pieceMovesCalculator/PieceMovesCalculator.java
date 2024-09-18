package chess.pieceMovesCalculator;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;

public abstract class PieceMovesCalculator {
    ChessBoard board;
    ChessPosition position;
    ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public PieceMovesCalculator(ChessBoard board, ChessPosition position) {
        this.position = position;
        this.board = board;
    }

    public abstract ArrayList<ChessMove> calculateLegalMoves();

}
