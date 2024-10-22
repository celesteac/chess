package server;

import chess.ChessGame;

public record JoinRequest(ChessGame.TeamColor playerColor, Integer gameID, String authToken) {
    JoinRequest assignAuth(String newAuthToken){
        return new JoinRequest(playerColor, gameID, newAuthToken);
    }
}
