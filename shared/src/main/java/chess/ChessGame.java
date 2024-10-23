package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //gets the list from pieceMoves
        //checks that each move does not cause check

        ChessPiece piece = board.getPiece(startPosition); //checks
        if( piece ==null ){
            return null;
        }

        Collection<ChessMove> valids = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> toRemove = new ArrayList<>();
        TeamColor movingTeam = piece.getTeamColor();

        for(ChessMove move: valids){
            System.out.println("checking move: " + move);
            //examine whether there would be check after the move
            ChessBoard hypotheticalBoard = getHypotheticalBoard(board, move);
            ChessPosition kingPosition = findTeamKing(movingTeam, hypotheticalBoard);

            if(kingPosition != null) {
                if (new AssessCheck(kingPosition, hypotheticalBoard).assessCheckAll()) {
                    System.out.println("adding to toRemove: " + move);
                    toRemove.add(move);
                }
            }
        }
        if(!toRemove.isEmpty()) {
            for (ChessMove move : toRemove) {
                valids.remove(move);
            }
        }

        System.out.println("valid moves: " + valids);
        return valids;
    }

    private ChessBoard getHypotheticalBoard(ChessBoard startingBoard, ChessMove move){

        ChessBoard hypotheticalBoard = startingBoard.clone();
        executeMove(move, hypotheticalBoard);

        System.out.println("old board: " + board);
        System.out.println("hypothetical w/ move: " + hypotheticalBoard);

        return hypotheticalBoard;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valids = validMoves(move.getStartPosition());


        if (valids != null && valids.contains(move)) {

            ChessPiece movingPiece = board.getPiece(move.getStartPosition());
            if(movingPiece.getTeamColor() != teamTurn){
                throw new InvalidMoveException( movingPiece.getTeamColor().toString() + movingPiece + "tried to move out of turn");
            }

            executeMove(move, board);
            changeTeamColor();
        } else {
            throw new InvalidMoveException("invalid move"); //WHY DOES THIS WORK?? Where is the exception handled?
        }

    }

    private void executeMove(ChessMove move, ChessBoard board){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        ChessPiece.PieceType promo = move.getPromotionPiece();
        ChessPiece piece = board.getPiece(move.getStartPosition());

        board.addPiece(start, null);

        if(promo == null) {
            board.addPiece(end, piece);
        }
        else {
            board.addPiece(end, new ChessPiece(teamTurn, promo));
        }
    }

    private void changeTeamColor(){

        TeamColor nextTeam = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        setTeamTurn(nextTeam);
    }


    private ChessPosition findTeamKing(TeamColor color, ChessBoard board){
        System.out.println("finding king " + color.toString());
        for(int i = 1; i<9 ; i++){
            for(int j = 1; j<9; j++){
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if((tempPiece != null) && (tempPiece.getTeamColor() == color) && (tempPiece.getPieceType() == ChessPiece.PieceType.KING)){
                    System.out.println("king at: " + tempPosition);
                    return tempPosition;
                }
            }
        }
        return null; //this would be a big problem. Throw a custom exception?
    }

    private Collection<ChessPosition> findAllTeamPieces(TeamColor color){
        ArrayList<ChessPosition> allPieces = new ArrayList<>();
        for(int i = 1; i<9 ; i++){
            for(int j = 1; j<9; j++){
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null){
                    if(tempPiece.getTeamColor() == color){
                        allPieces.add(tempPosition);
                    }
                }
            }
        }
        System.out.println("all pieces: " + allPieces);
        return allPieces;
    }

    private boolean allTeamPiecesStuck(TeamColor teamColor){
        Collection<ChessPosition> allPiecePositions = findAllTeamPieces(teamColor);
        boolean noPiecesCanMove = true;
        for(ChessPosition piecePos : allPiecePositions){
            Collection<ChessMove> pieceMoves = validMoves(piecePos);
            if(!pieceMoves.isEmpty()){
                noPiecesCanMove = false;
                break;
            }
        }
        return noPiecesCanMove;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        System.out.println(board);
        System.out.println("checking status " + teamColor.toString());
        ChessPosition kingPosition = findTeamKing(teamColor, board);
        return new AssessCheck(kingPosition, board).assessCheckAll();
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //check if the king is currently in check
        //and all the team's pieces have empty valid moves
        if(isInCheck(teamColor)){
            return allTeamPiecesStuck(teamColor);
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //if not in check and not in checkmate, but valid moves empty for all pieces
        if(!isInCheck(teamColor) && !isInCheckmate(teamColor)){
            return allTeamPiecesStuck(teamColor);
        }

        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
