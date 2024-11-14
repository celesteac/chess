import chess.*;
import ui.EscapeSequences;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println(EscapeSequences.WHITE_QUEEN + " 240 Chess client.Client: " + EscapeSequences.WHITE_KING);

        Repl repl = new Repl();
        repl.run();
    }
}