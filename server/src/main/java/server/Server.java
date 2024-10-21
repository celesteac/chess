package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.Service;
import service.ServiceException;
import spark.*;

public class Server {
    private final Service service = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.post("/user", (req, res) -> registerUser(req, res));
//        Spark.post("/test", (req, res) -> testPrintReq(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    //this is where handler functions go
    //they will be called from the endpoints
    //they use the Service methods
    //convert inputs to JSON with GSON before passing them on?

    private Object registerUser(Request req, Response res) {
        try {
            var g = new Gson();
            UserData newUser = g.fromJson(req.body(), UserData.class);
            if(!checkValidRegisterRequest(newUser)) {
                throw new ServerException("bad request");
            }

            AuthData auth = service.registerUser(newUser);
            return g.toJson(auth);
        }
        catch(ServiceException e){
            //403 = if user existed, forbidden
            return createErrorResponse(403, e.getMessage(), res);
        }
        catch (ServerException e){
            //400 = bad request
            return createErrorResponse(400, e.getMessage(), res);
        }
    }

    private String createErrorResponse(Integer status, String message, Response res){
        var gson = new Gson();
        ErrorResponse messageObj = new ErrorResponse(message);
        res.status(status);
        res.body(gson.toJson(messageObj));
        res.type("application/json");
        return res.body();
    }

//    private UserData testPrintReq(Request req, Response res){
//        var g = new Gson();
//        UserData newUser = g.fromJson(req.body(), UserData.class);
//        res.type("application/json");
//        return newUser;
//    }

    private boolean checkValidRegisterRequest(UserData user) {
        return (user.username() != null
                && user.password() != null
                && user.email() != null);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
