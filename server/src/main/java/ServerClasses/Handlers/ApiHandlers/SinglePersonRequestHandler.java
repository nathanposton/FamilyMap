package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.AuthorizingRequestHandler;
import Request.SinglePersonServiceRequest;
import Result.Response;
import ServerClasses.Service.Services.SinglePersonService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class SinglePersonRequestHandler extends AuthorizingRequestHandler {

    protected Response getResponse(HttpExchange exchange, String authToken) throws IOException {
        String urlPath = exchange.getRequestURI().toString();
        String personID = urlPath.substring(8); //TODO: verify this changes '/person/etc' to 'etc';

        return SinglePersonService.singlePerson(new SinglePersonServiceRequest(personID, authToken));

    }
}

