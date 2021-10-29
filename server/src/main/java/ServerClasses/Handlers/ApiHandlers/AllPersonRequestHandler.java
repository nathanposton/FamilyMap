package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.AuthorizingRequestHandler;
import Request.AllPersonServiceRequest;
import Result.Response;
import ServerClasses.Service.Services.AllPersonService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AllPersonRequestHandler extends AuthorizingRequestHandler {

    protected Response getResponse(HttpExchange exchange, String authToken) throws IOException {
        return AllPersonService.allPerson(new AllPersonServiceRequest(authToken));
    }
}
