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

        // Register your endpoints and handle exceptions here.
        //this is where the Spark.post("/path", function) and other go

        Spark.post("/user", (req, res) -> registerUser(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    //this is where handler function go
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
            return g.toJson(s.getMessage());
        }

        return g.toJson(a);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
