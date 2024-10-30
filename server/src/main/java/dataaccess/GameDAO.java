package dataaccess;

import model.GameData;

import java.util.Map;

public interface GameDAO extends DAO {
    public void addGame(GameData gameData);
    public void updateGame(GameData gameData);
    public GameData getGame(Integer gameID);
    public Map<Integer, GameData> getAllGames();
    public void clear();
    public int getNumGames();
}
