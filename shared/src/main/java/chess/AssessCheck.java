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
                int tempRow = row + rowMod;
                int tempCol = col + colMod;
                ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

                if (checkBounds(tempRow, tempCol)){
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if(tempPiece != null){
                        contLoop = false;
                        if(tempPiece.getTeamColor() != kingColor){
                            if(tempPiece.getPieceType() == ChessPiece.PieceType.BISHOP
                                         || tempPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
                                return true;
                            }
                        }
                    }
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





//    private int[][] pieceDirections(ChessPiece.PieceType pieceType){
//        int[] up = {1,0};
//        int[] down = {-1,0};
//        int[] right = {0,1};
//        int[] left = {0,-1};
//        int[] diagUpRight = {1,1};
//        int[] diagUpLeft = {1,-1};
//        int[] diagDownRight = {-1,1};
//        int[] diagDownLeft = {-1,-1};
//
//        int[][] test = {up, right};
//
//        return switch(pieceType) {
//            case QUEEN -> {up, down, left, right, diagUpLeft, diagDownLeft, diagDownRight, diagUpRight};
//            case KNIGHT -> {up};
////            case BISHOP -> {down};
////            case ROOK -> {};
////            case PAWN -> {};
//            default -> null;
//        };
//    }
//
//    private boolean checkAttack(int[][] directions, ChessPiece.PieceType type){
//        //for loop 7 times
//            //check at the position row + rowDir * i and col same
//
//
//        return false;
//    }

//    private Collection<ChessPosition> check


}
