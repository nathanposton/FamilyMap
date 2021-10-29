package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.AuthorizingRequestHandler;
import Request.AllEventServiceRequest;
import Result.Response;
import ServerClasses.Service.Services.AllEventService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AllEventRequestHandler extends AuthorizingRequestHandler {

    protected Response getResponse(HttpExchange exchange, String authToken) throws IOException {
        return AllEventService.allEvent(new AllEventServiceRequest(authToken));
    }
}