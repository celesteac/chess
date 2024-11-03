package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameDAOSQL implements GameDAO{

    public void addGame(GameData gameData) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO games (gameID, gameName, game, whiteUsername, blackUsername) VALUES (?, ?, ?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)){

                var jsonGameObj = new Gson().toJson(gameData.game());
                preparedStatement.setInt(1, gameData.gameID());
                preparedStatement.setString(2, gameData.gameName());
                preparedStatement.setString(3, jsonGameObj);
                preparedStatement.setString(4, gameData.whiteUsername());
                preparedStatement.setString(5, gameData.blackUsername());

                preparedStatement.executeUpdate();
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String deleteStatement = "DELETE FROM games WHERE gameID = ?";
            try(var preparedStatement = conn.prepareStatement(deleteStatement) ) {
                preparedStatement.setInt(1, gameData.gameID());
                preparedStatement.executeUpdate();
            }
            addGame(gameData);
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    private GameData getGameDataFromSqlResults(ResultSet rs, int gameID) throws SQLException {
        String foundGameName = rs.getString("gameName");
        String foundJsonGameObj = rs.getString("game");
        String foundWhiteUsername = rs.getString("whiteUsername");
        String foundBlackUsername = rs.getString("blackUsername");

        ChessGame foundGame = new Gson().fromJson(foundJsonGameObj, ChessGame.class);

        String checkedWhiteUsername = Objects.equals(foundWhiteUsername, "null") ? null : foundWhiteUsername;
        String checkedBlackUsername = Objects.equals(foundBlackUsername, "null") ? null : foundBlackUsername;

        return new GameData(foundGame, checkedWhiteUsername, checkedBlackUsername, foundGameName, gameID);
    }

    public GameData getGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT gameName, game, whiteUsername, blackUsername FROM games WHERE gameID = ?";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.setInt(1, gameID);
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return getGameDataFromSqlResults(rs, gameID);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public Map<Integer, GameData> getAllGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                try(var rs = preparedStatement.executeQuery()){
                    Map<Integer, GameData> allGames = new HashMap<>();
                    while(rs.next()){
                        int foundGameID = rs.getInt("gameID");
                        GameData foundGameData = getGameDataFromSqlResults(rs, foundGameID);
                        allGames.put(foundGameID, foundGameData);
                    }
                    return allGames;
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE games";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public int getNumGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT COUNT(0) AS count FROM games";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return rs.getInt("count");
                    } else {
                        return 0;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

}
