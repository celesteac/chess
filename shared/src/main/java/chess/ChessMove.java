package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove implements Cloneable{
    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promotion = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public String toString() {
        String move = "[" + start + ">" + end + "]";
        move = promotion == null ? move : move + promotion.toString();
        return move;
    }

    @Override
    public ChessMove clone(){
        try {
            ChessMove cloneMove = (ChessMove) super.clone();

            ChessPosition cloneStart = start.clone();
            ChessPosition cloneEnd = end.clone();
            ChessPiece.PieceType clonePromoType = promotion;

            cloneMove.start = cloneStart;
            cloneMove.end = cloneEnd;
            cloneMove.promotion = clonePromoType;

            return cloneMove;

        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        };
        if(!(o instanceof ChessMove chessMove)) {
            return false;
        };
        return (start.equals(chessMove.start) && end.equals(chessMove.end) && promotion == chessMove.promotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, promotion);
    }
}
