package dataaccess;

import model.GameData;

import java.util.Map;

public class GameDAOSQL implements GameDAO{
    public void addGame(GameData gameData) {

    }

    public void updateGame(GameData gameData) {

    }

    public GameData getGame(Integer gameID) {
        return null;
    }

    public Map<Integer, GameData> getAllGames() {
        return Map.of();
    }

    public void clear() {

    }

    public int getNumGames() {
        return 0;
    }
}
