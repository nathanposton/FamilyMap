package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.PostRequestHandler;
import Request.FillRequest;
import Result.Response;
import ServerClasses.Service.Services.FillService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class FillRequestHandler extends PostRequestHandler {

    public Response getResponse(HttpExchange exchange) throws IOException {
        String username;
        int generations;

        StringBuilder urlPath = new StringBuilder(exchange.getRequestURI().toString());

        urlPath.delete(0, 6); //TODO: verify this changes '/fill/etc/' to 'etc/'

        int slashIndex = urlPath.indexOf("/");
        if (slashIndex == -1) {
            username = urlPath.toString();
            generations = 4;
        } else {
            username = urlPath.substring(0, slashIndex);
            urlPath.delete(0, slashIndex + 1);

            if (urlPath.length() < 1) {
                generations = 4;
            } else slashIndex = urlPath.indexOf("/");
            if (slashIndex == -1) {
                generations = Integer.parseInt(urlPath.toString());
            } else {
                generations = Integer.parseInt(urlPath.substring(0, slashIndex).toString());
            }
        }
        return FillService.fill(new FillRequest(username, generations));

    }
}
