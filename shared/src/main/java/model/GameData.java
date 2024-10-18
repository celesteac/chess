package model;

import chess.ChessGame;

public record GameData(ChessGame game, String whiteUsername, String blackUsername, String gameName, int gameID) implements DataRepresentationObj {
}
