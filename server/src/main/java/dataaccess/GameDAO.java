package dataaccess;

import model.GameData;

import java.util.Map;

public interface GameDAO extends DAO {
    public void addGame(GameData gameData) throws DataAccessException;
    public void updateGame(GameData gameData)throws DataAccessException ;
    public GameData getGame(Integer gameID) throws DataAccessException;
    public Map<Integer, GameData> getAllGames() throws DataAccessException;
    public void clear() throws DataAccessException;
    public int getNumGames() throws DataAccessException;
}
