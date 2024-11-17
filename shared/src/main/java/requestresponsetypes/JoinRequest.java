package requestresponsetypes;

import chess.ChessGame;

public record JoinRequest(ChessGame.TeamColor playerColor, Integer gameID, String authToken) {
    public JoinRequest assignAuth(String newAuthToken){
        return new JoinRequest(playerColor, gameID, newAuthToken);
    }
}
