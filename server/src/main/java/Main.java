import chess.*;
import server.Server;

public class Main {
    private static final dataAccessType dataType = dataAccessType.SQL;

    public enum dataAccessType {
        MEMORY,
        SQL
    }

    public static void main(String[] args) {
        new Server().run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }

    public static dataAccessType getDataAccessType(){
        return dataType;
    }
}