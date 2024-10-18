package server;

import com.google.gson.Gson;
import model.UserData;
import service.Service;
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

    private UserData registerUser(Request req, Response res){
        UserData newUser = new Gson().fromJson(req.body(), UserData.class);


        return s.registerUser(newUser);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
