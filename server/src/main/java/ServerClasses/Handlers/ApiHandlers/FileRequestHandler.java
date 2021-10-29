package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileRequestHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                //TODO: test if headers necessary
//                Headers reqHeaders = exchange.getRequestHeaders();

                String urlPath = exchange.getRequestURI().toString();

                if (urlPath == null || urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                String filePath = "web" + urlPath;

                File file = new File(filePath);
                if (file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();

                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    file = new File("web/HTML/404.html");
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}