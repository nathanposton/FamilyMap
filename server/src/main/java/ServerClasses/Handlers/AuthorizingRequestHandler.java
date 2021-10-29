package ServerClasses.Handlers;

import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.Database;
import ServerClasses.Service.ServiceTools.JsonSerializer;
import Model.AuthToken;
import Result.Response;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class AuthorizingRequestHandler extends RequestHandler {



    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");

                    Response r = getResponse(exchange, authToken);

                    if (isAuthorized(authToken)) {

                        if (r.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                        else {
                            //TODO: error resolution/calls/declarations???
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(JsonSerializer.serialize(r), respBody);
                    respBody.close();
                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch(IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    protected abstract Response getResponse (HttpExchange exchange, String authToken) throws IOException;

    public boolean isAuthorized(String authToken) {
        Database db = new Database();
        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());

            AuthToken dbToken = authTokenDao.verify(authToken);

            db.closeConnection(false);

            return dbToken != null;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
        }
        return false;
    }


}