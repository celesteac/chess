package chess;

import java.util.Collection;

public class AssessCheck {
    ChessPosition king;
    ChessBoard board;
    ChessGame.TeamColor kingColor;
    ChessGame.TeamColor otherTeamColor;
    int row;
    int col;

    AssessCheck(ChessPosition kingPosition, ChessBoard board){
        this.board = board;
        this.king = kingPosition;
        this.kingColor = board.getPiece(this.king).getTeamColor();
        this.otherTeamColor =  kingColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        this.row = kingPosition.getRow();
        this.col = kingPosition.getColumn();
    }

    public boolean assessCheckAll(){
        return (checkBishopQueen() || checkRookQueen() || checkKnight() || checkPawn());
    }

    private boolean checkBounds(int tempRow, int tempCol){
        return (tempRow < 9 && tempCol < 9 && tempRow > 0 && tempCol > 0);
    }


    private boolean checkBishopQueen(){
        for(int j = 1; j < 5; j++){
            for(int i = 1; i < 8; i++){

                boolean contLoop = true;

                int[] direction = switch (j){
                    case 1 -> new int[] {i,i};    //up right
                    case 2 -> new int[] {i, -i};  //up left
                    case 3 -> new int[] {-i, i};  //down right
                    case 4 -> new int[] {-i, -i}; //down left
                    default -> new int[] {0, 0};  //error
                };

                int rowMod = direction[0];
                int colMod = direction[1];
                int tempRow = king.convertRowIndices(row + rowMod);
                int tempCol = king.convertColToChessIndices(col + colMod);
                ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);


                if (checkBounds(tempRow, tempCol)){
                    System.out.println(tempPosition);
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if(tempPiece != null){
                        System.out.println("piece: " + tempPiece.getTeamColor() + tempPiece.getPieceType());
                        contLoop = false;
                        if(tempPiece.getTeamColor() != kingColor){
                            if(tempPiece.getPieceType() == ChessPiece.PieceType.BISHOP
                                         || tempPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                System.out.println("attacking piece: " + tempPiece.getTeamColor() + tempPiece.getPieceType() + " at " + tempPosition);
                                return true;
                            }
                        }
                    }
                }
                else{
                    contLoop = false;
                }
                if(!contLoop){
                    break;
                }
            }
        }

        return false;
    }





    private boolean checkRookQueen(){
        return false;
    }

    private boolean checkKnight(){
        return false;
    }

    private boolean checkPawn(){
        return false;
    }


}
