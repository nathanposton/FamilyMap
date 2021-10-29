import ServerClasses.Handlers.ApiHandlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {

    public static void main(String[] args) {
        String usage = "Usage: java [FamilyMapServer] <port number>";
        if (args.length != 1) {
            System.out.println(usage);
        } else {
            try {
                int port = Integer.parseInt(args[0]);
                FamilyMapServer familyMapServer = new FamilyMapServer();

                familyMapServer.startServer(port);

            }
            catch (NumberFormatException num) {
                System.out.println(usage);
                num.printStackTrace();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/user/login", new LoginRequestHandler());
        server.createContext("/fill", new FillRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/person/", new SinglePersonRequestHandler());
        server.createContext("/event/", new SingleEventRequestHandler());
        server.createContext("/person", new AllPersonRequestHandler());
        server.createContext("/event", new AllEventRequestHandler());
    }
}
