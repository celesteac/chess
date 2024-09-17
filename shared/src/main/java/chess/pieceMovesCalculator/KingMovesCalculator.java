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
        //if a piece is by the edge or a piece, it cannot move there
        //a king can move into any of the spaces around it

        //write a loop that checks all the spaces around the king
            //if the space is empty, you can move into it

//        legalMoves.add(new ChessMove(position, new ChessPosition(position.getColumn(), position.getRow()), null));
        legalMoves.add(new ChessMove(position, position, null));

        return this.legalMoves;
    }

    //accounts for pieces blocking path
    //does not ask if the king is being attacked or whose turn it is
    //does it care if you can take a piece? I think it doesn't take pieces?
}
