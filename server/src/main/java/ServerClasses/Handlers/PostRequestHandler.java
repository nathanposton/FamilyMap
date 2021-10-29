package ServerClasses.Handlers;

import ServerClasses.Service.ServiceTools.JsonSerializer;
import Result.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public abstract class PostRequestHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                Response r = getResponse(exchange);

                if (r.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    //TODO: error resolution/calls/declarations???
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                OutputStream respBody = exchange.getResponseBody();
                writeString(JsonSerializer.serialize(r), respBody);
                respBody.close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

     protected abstract Response getResponse(HttpExchange exchange) throws IOException;
}