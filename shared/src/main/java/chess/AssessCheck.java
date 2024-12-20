package chess;

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
        return (checkBishopQueen() || checkRookQueen() || checkKnight() || checkKing() || checkPawn());
//        return (checkBishopQueen() || checkRookQueen() || checkKnight() || checkPawn());
    }

    private boolean checkBounds(int tempRow, int tempCol){
        return (tempRow < 9 && tempCol < 9 && tempRow > 0 && tempCol > 0);
    }

    private int[] getDirectionBishopQueen(int j, int i){
        return switch (j){
            case 1 -> new int[] {i,i};    //up right
            case 2 -> new int[] {i, -i};  //up left
            case 3 -> new int[] {-i, i};  //down right
            case 4 -> new int[] {-i, -i}; //down left
            default -> new int[] {0, 0};  //error
        };
    }

    private int[] getDirectionRookQueen(int j, int i){
        return switch (j){
            case 1 -> new int[] {i,0};    //up
            case 2 -> new int[] {0,-i};  //left
            case 3 -> new int[] {-i,0};  //down
            case 4 -> new int[] {0,i}; //right
            default -> new int[] {0, 0};  //error
        };
    }

    private boolean checkBishopQueen(){
        for(int j = 1; j < 5; j++){
            if(logicCheckQueenBishopRook(j, ChessPiece.PieceType.BISHOP)){
                return true;
            }
        }
        return false;
    }


    private boolean checkRookQueen(){
        for(int j = 1; j < 5; j++){ //checking 4 directions
            if(logicCheckQueenBishopRook(j, ChessPiece.PieceType.ROOK)){
                return true;
            }
        }
        return false;
    }

    private boolean logicCheckQueenBishopRook(int j, ChessPiece.PieceType bishopOrRook){
        for(int i = 1; i < 8; i++){

            boolean contLoop = true;

            int[] direction = switch (bishopOrRook){
                case ROOK -> direction= getDirectionRookQueen(j,i);
                case BISHOP -> direction= getDirectionBishopQueen(j,i);
                default -> direction = new int[] {0,0}; //error
            };

            int rowMod = direction[0];
            int colMod = direction[1];
            int tempRow = king.convertRowIndices(row + rowMod);
            int tempCol = king.convertColToChessIndices(col + colMod);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);


            if (checkBounds(tempRow, tempCol)){
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null){
                    contLoop = false;
                    if((tempPiece.getTeamColor() != kingColor) && (tempPiece.getPieceType() == bishopOrRook
                            || tempPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                        return true;
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
        return false;
    }

    private boolean checkKing(){

        for(int j = 1; j < 9; j++){ //check 8 different spots
            int[] direction = switch (j) {
                case 1 -> new int[] {1,0};
                case 2 -> new int[] {1,1};
                case 3 -> new int[] {0,1};
                case 4 -> new int[] {-1,1};
                case 5 -> new int[] {-1,0};
                case 6 -> new int[] {-1,-1};
                case 7 -> new int[] {0,-1};
                case 8 -> new int[] {1,-1};
                default -> new int[] {0,0};
            };

            int tempRow = king.convertRowIndices(row + direction[0]);
            int tempCol = king.convertColToChessIndices(col + direction[1]);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

            if (checkBounds(tempRow, tempCol)){
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null){
                    if((tempPiece.getTeamColor() != kingColor) && (tempPiece.getPieceType() == ChessPiece.PieceType.KING)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkKnight(){

        for(int j = 1; j < 9; j++){ //check 8 different spots
            int[] direction = switch (j) {
                case 1 -> new int[] {1,2};
                case 2 -> new int[] {1,-2};
                case 3 -> new int[] {-1,2};
                case 4 -> new int[] {-1,-2};
                case 5 -> new int[] {2,1};
                case 6 -> new int[] {2,-1};
                case 7 -> new int[] {-2,1};
                case 8 -> new int[] {-2,-1};
                default -> new int[] {0,0};
            };

                int tempRow = king.convertRowIndices(row + direction[0]);
                int tempCol = king.convertColToChessIndices(col + direction[1]);
                ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

                if (checkBounds(tempRow, tempCol)){
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if(tempPiece != null){
                        if((tempPiece.getTeamColor() != kingColor) && (tempPiece.getPieceType() == ChessPiece.PieceType.KNIGHT)){
                            return true;
                        }
                    }
                }

            }


        return false;
    }





    private boolean checkPawn(){

        //if white, check the diagonals above
        //if black, check the diagonals below

        int[][] directions = switch (kingColor){
            case BLACK -> new int[][] {{1,1},{1,-1}};
            case WHITE -> new int[][] {{-1,1},{-1,-1}};
        };

        for(int[] direction : directions){
            int tempRow = king.convertRowIndices(row + direction[0]);
            int tempCol = king.convertColToChessIndices(col + direction[1]);
            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);

            if (checkBounds(tempRow, tempCol)){
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null){
                    if((tempPiece.getTeamColor() != kingColor) && (tempPiece.getPieceType() == ChessPiece.PieceType.PAWN)){
                        return true;
                    }
                }
            }

        }


        return false;
    }


}
