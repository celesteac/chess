package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(ChessGame game, String whiteUsername, String blackUsername, String gameName, int gameID) implements DataRepresentationObj {

    public GameData updateGamePlayer(ChessGame.TeamColor playerColor, String username) {
        if(playerColor == ChessGame.TeamColor.WHITE){
            return new GameData(game, username, blackUsername, gameName, gameID);
        }
        else {
            return new GameData(game, whiteUsername, username, gameName, gameID);
        }
    }

    public ChessGame.TeamColor getPlayerColor(String username){
        if(Objects.equals(username, whiteUsername)){
            return ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(username, blackUsername)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

}
