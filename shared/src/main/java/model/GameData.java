package model;

import chess.ChessGame;

public record GameData(ChessGame game, String whiteUsername, String blackUsername, String gameName, int gameID) implements DataRepresentationObj {
    public GameData updateGame(ChessGame.TeamColor playerColor, String username) {
        if(playerColor == ChessGame.TeamColor.WHITE){
            return new GameData(game, username, blackUsername, gameName, gameID);
        }
        else {
            return new GameData(game, whiteUsername, username, gameName, gameID);
        }
    }
}
