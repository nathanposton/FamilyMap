package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.PostRequestHandler;
import Result.Response;
import ServerClasses.Service.Services.ClearService;
import com.sun.net.httpserver.HttpExchange;

public class ClearRequestHandler extends PostRequestHandler {

    protected Response getResponse(HttpExchange exchange) {
        return ClearService.clear();
    }
}