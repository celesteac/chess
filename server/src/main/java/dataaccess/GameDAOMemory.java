package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDAOMemory implements GameDAO{
    Map<Integer, GameData> games = new HashMap<>();

    public void addGame(GameData gameData){
        games.put(gameData.gameID(), gameData);
    }

    public GameData getGame(Integer gameID){
        return games.get(gameID);
    }

    public Map<Integer, GameData> getAllGames(){
        return games;
    }

    public void clear(){
        games.clear();
    }

    public int getNumGames(){
        return games.size();
    }
}
