package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.AuthorizingRequestHandler;
import Request.SingleEventServiceRequest;
import Request.SinglePersonServiceRequest;
import Result.Response;
import ServerClasses.Service.Services.SingleEventService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class SingleEventRequestHandler extends AuthorizingRequestHandler {

    protected Response getResponse(HttpExchange exchange, String authToken) throws IOException {
        String urlPath = exchange.getRequestURI().toString();
        String eventID = urlPath.substring(7);

        return SingleEventService.singleEvent(new SingleEventServiceRequest(eventID, authToken));

    }
}

