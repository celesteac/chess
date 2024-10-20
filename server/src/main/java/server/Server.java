package server;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import service.Service;
import service.ServiceException;
import spark.*;

public class Server {
    private final Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");


        Spark.post("/user", (req, res) -> registerUser(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    //this is where handler functions go
    //they will be called from the endpoints
    //they use the Service methods
    //convert inputs to JSON with GSON before passing them on?

    private Object registerUser(Request req, Response res) {
        var g = new Gson();
        UserData newUser = g.fromJson(req.body(), UserData.class);
        //throw error here if bad request? helper function

        UserData a = new UserData(null, null, null);

        try {
             a = s.registerUser(newUser);
        }
        catch(ServiceException s){
            //403 = if user existed, forbidden
            return createErrorMessage(403, s.getMessage(), res);

        }

        return g.toJson(a);
    }

    private String createErrorMessage(Integer status, String message, Response res){
        var gson = new Gson();
        ErrorResponse messageObj = new ErrorResponse(message);
        res.status(status);
        res.body(gson.toJson(messageObj));
        res.type("application/json");
        return res.body();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
