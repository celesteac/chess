package service;

import chess.ChessGame;
import chess.ChessPiece;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.CreateRequest;
import server.JoinRequest;

import java.util.*;

public class Service {
    UserDAOMemory userDAO = new UserDAOMemory();
    AuthDAOMemory authDAO = new AuthDAOMemory();
    GameDAOMemory gameDAO = new GameDAOMemory();

    /// DIRECT IMPLEMENTATION FUNCTIONS ////////////////

    public AuthData registerUser(UserData newUser) throws ServiceException {

        if(!checkValidRegisterRequest(newUser)) {
            throw new ServiceException("Error: bad request", 400);
        }
        if( userDAO.getUser(newUser.username()) != null ){
            throw new ServiceException("Error: user already exists", 403);
        }

        userDAO.addUser(newUser);
        AuthData auth = new AuthData(generateAuthToken(), newUser.username());
        authDAO.addAuthData(auth);
        //test this?

        return auth;
    }


    public AuthData login(UserData user) throws ServiceException{
        UserData foundUser = userDAO.getUser(user.username());
        if (foundUser == null){
            throw new  ServiceException("Error: unauthorized", 401);
        }
        if(!Objects.equals(user.password(), foundUser.password())){
            throw new ServiceException("Error: wrong password", 401);
        }

        String authToken = generateAuthToken();
        AuthData auth = new AuthData(authToken, user.username());
        authDAO.addAuthData(auth);
        return auth;
    }

    public void logout(String authToken) throws ServiceException{
        AuthData foundAuth = authDAO.getAuthData(authToken);
        if(foundAuth == null){
            throw new ServiceException("Error: unauthorized", 401);
        }
        if(!authDAO.deleteAuth(foundAuth)){
            throw new ServiceException("Error: internal server error. Try logout again", 500);
        }
    }


    public int createGame(CreateRequest createReq) throws ServiceException{
        if(!checkValidCreateRequest(createReq)){
            throw new ServiceException("Error: bad request", 400);
        }
        if(authDAO.getAuthData(createReq.authToken()) == null){
            throw new ServiceException("Error: unauthorized", 401);
        }
        GameData newGame = newGameData(createReq.gameName());
        gameDAO.addGame(newGame);
        return newGame.gameID();
    }

    public int joinGame(JoinRequest joinReq) throws ServiceException{
        if(!checkValidJoinRequest(joinReq)){
            throw new ServiceException("Error: bad request", 400);
        }

        AuthData foundAuth = authDAO.getAuthData(joinReq.authToken());
        if(foundAuth == null){
            throw new ServiceException("Error: unauthorized", 401);
        }

        GameData foundGame =  gameDAO.getGame(joinReq.gameID());
        if(foundGame == null){
            throw new ServiceException("Error: Game doesn't exist", 500);
        }

        ChessGame.TeamColor playerColor = joinReq.playerColor();
        if(!checkPlayerColorAvailable(foundGame, playerColor)){
            throw new ServiceException("Error: color already taken", 403);
        }

        GameData newGame = foundGame.updateGame(joinReq.playerColor(), foundAuth.username());
        gameDAO.updateGame(newGame);
        return newGame.gameID();

    }

    public ListResponse listGames(String authToken) throws ServiceException{
        if(authDAO.getAuthData(authToken) == null){
            throw new ServiceException("Error: unauthorized", 401);
        }
        Map<Integer, GameData> games = gameDAO.getAllGames();
        Collection<GameData> gameDatas = games.values();
        ArrayList<ListResponseItems> listResponseGames = new ArrayList<>();
        for(GameData game : gameDatas){
            ListResponseItems gameToAdd = new ListResponseItems(
                    game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            listResponseGames.add(gameToAdd);
        }


        return new ListResponse(listResponseGames);
    }

    public void clearDB() throws ServiceException{
        //clear each of the thingies
        //is this void
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        if(gameDAO.getNumGames() > 0 || authDAO.getNumAuths() > 0 || userDAO.getNumUsers() >0){
            throw new ServiceException("Error: Database failed to clear", 500);
        }
    }


    /// HELPER FUNCTIONS //////////


    private boolean checkPlayerColorAvailable(GameData game, ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE){
            return game.whiteUsername() == null;
        }
        else{
            return game.blackUsername() == null;
        }
    }


    private GameData newGameData(String gameName){
        int gameID;

        boolean isUnique = false;
        do {
            gameID = generateGameID();
            isUnique = checkGameIDUnique(gameID);
        }
        while (!isUnique);

        return new GameData(new ChessGame(),
                null, null,
                gameName, gameID );
    }

    private int generateGameID(){
        return new Random().nextInt(9000) + 1000;
    }

    private boolean checkGameIDUnique(int gameID){
        return gameDAO.getGame(gameID) == null;
    }

    private boolean checkValidCreateRequest(CreateRequest createReq){
//        System.out.println(createReq.gameName());
//        System.out.println(createReq.authToken());
        return (createReq.gameName() != null && !createReq.gameName().isEmpty()
                && createReq.authToken() != null && !createReq.authToken().isEmpty());
    }

    //make this an internal class??
    private boolean checkValidRegisterRequest(UserData user) {
        return (user.username() != null
                && user.password() != null
                && user.email() != null);
    }

    private boolean checkValidJoinRequest(JoinRequest joinReq){
        return (joinReq.authToken() != null
                && joinReq.playerColor() != null
                && joinReq.gameID() != null);
    }

    //not this one
    public String generateAuthToken(){
        return UUID.randomUUID().toString();
    }
}
