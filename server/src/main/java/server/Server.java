package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import requestresponsetypes.*;
import service.Service;
import service.ServiceException;
import spark.*;

public class Server {

    private final Service service = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearDB);
        Spark.exception(ServiceException.class, this::exceptionHandler);
        Spark.exception(DataAccessException.class, this::dataExceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }


    /// Handler Functions /////////

    private void exceptionHandler(ServiceException ex, Request req, Response res){
        var gson = new Gson();
        res.status(ex.getStatus());
//        ErrorResponse messageObj = new ErrorResponse(ex.getMessage());
//        res.body(gson.toJson(messageObj));
        res.body(gson.toJson(ex.getMessage()));
        res.type("application/json");
        System.out.println();
    }

    private void dataExceptionHandler(DataAccessException ex, Request req, Response res){
        var gson = new Gson();
        String errorMessage = "Database Error: " + ex.getMessage();
        ErrorResponse messageObj = new ErrorResponse(errorMessage);
        res.status(500);
        res.body(gson.toJson(messageObj));
        res.type("application/json");
    }


    private Object clearDB(Request req, Response res) throws ServiceException, DataAccessException{
            service.clearDB();
            res.status(200);
            return "";
    }

    //maybe combine the login and register or make an interface
    private Object login(Request req, Response res) throws ServiceException, DataAccessException {
        var g = new Gson();
        UserData newUser = g.fromJson(req.body(), UserData.class);

        AuthData auth = service.login(newUser);
        return g.toJson(auth);
    }

    private Object registerUser(Request req, Response res) throws ServiceException, DataAccessException {
            var g = new Gson();
            UserData newUser = g.fromJson(req.body(), UserData.class);

            AuthData auth = service.registerUser(newUser);
            return g.toJson(auth);
    }

    private Object logout(Request req, Response res) throws ServiceException, DataAccessException {
        String authToken = req.headers("Authorization");
        service.logout(authToken);
        res.status(200);
        return "";
    }

    private Object createGame(Request req, Response res) throws ServiceException, DataAccessException {
        String authToken = req.headers("Authorization");
        CreateRequest createReq = new Gson().fromJson(req.body(), CreateRequest.class);
        createReq = createReq.addAuthToken(authToken);
        int gameID = service.createGame(createReq);
        CreateResponse createRes = new CreateResponse(gameID);
        return new Gson().toJson(createRes);
    }

    private Object joinGame(Request req, Response res) throws ServiceException, DataAccessException {
        String authToken = req.headers("Authorization");
        JoinRequest joinReq = new Gson().fromJson(req.body(), JoinRequest.class);
        JoinRequest joinReqWithAuth = joinReq.assignAuth(authToken);
        int gameID = service.joinGame(joinReqWithAuth);
        JoinResponse joinRes = new JoinResponse(gameID);

        return new Gson().toJson(joinRes);
    }

    private Object listGames(Request req, Response res) throws ServiceException, DataAccessException {
        String authToken = req.headers("Authorization");
        ListResponse games = service.listGames(authToken);
        return new Gson().toJson(games);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}







