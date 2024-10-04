package chess;

import chess.pieceMovesCalculator.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable{
    private final PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            return switch (type){
                case KING -> new KingMovesCalculator(board, myPosition).calculateMoves();
                case QUEEN -> new QueenMovesCalculator(board, myPosition).calculateMoves();
                case BISHOP -> new BishopMovesCalculator(board, myPosition).calculateMoves();
                case ROOK -> new RookMovesCalculator(board, myPosition).calculateMoves();
                case KNIGHT -> new KnightMovesCalculator(board, myPosition).calculateMoves();
                case PAWN -> new PawnMovesCalculator(board, myPosition).calculateMoves();
                default -> null;
            };
    }

    /// Overrides

    @Override
    public String toString() {
        String representation = switch (type) {
            case KING -> "k";
            case QUEEN -> "q";
            case BISHOP -> "b";
            case KNIGHT -> "n";
            case ROOK -> "r";
            case PAWN -> "p";
        };

        if(color == ChessGame.TeamColor.WHITE) {
            representation = representation.toUpperCase();
        }

        return representation;
    }

    @Override
    public ChessPiece clone(){
        try {
            return (ChessPiece) super.clone();

        } catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return type == piece.type && color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
