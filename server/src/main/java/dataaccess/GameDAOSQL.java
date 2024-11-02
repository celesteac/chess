package dataaccess;

import model.GameData;

import java.util.Map;

public class GameDAOSQL implements GameDAO{

    public void addGame(GameData gameData) throws DataAccessException {

    }

    public void updateGame(GameData gameData) throws DataAccessException {

    }

    public GameData getGame(Integer gameID) throws DataAccessException {

        return null;
    }

    public Map<Integer, GameData> getAllGames() throws DataAccessException {
        return Map.of();
    }

    public void clear() throws DataAccessException {

    }

    public int getNumGames() throws DataAccessException {
        return 0;
    }
}
