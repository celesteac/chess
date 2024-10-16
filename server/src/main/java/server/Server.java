package server;

import service.Service;
import spark.*;

public class Server {
    private Service s = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //this is where the Spark.post("/path", function) and other go

        Spark.post("/user", (req, res) -> registerUser(req, res));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    //this is where handler function go
    //they will be called from the endpoints
    //they use the Service methods
    //convert inputs to JSON with GSON before passing them on?

    private String registerUser(Request req, Response res){
        return """
                {"username": "john", "password":"piano", "email":"mail@mail.com"}
                """;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
