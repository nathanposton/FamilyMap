package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.PostRequestHandler;
import Request.RegisterRequest;
import Result.Response;
import ServerClasses.Service.ServiceTools.JsonSerializer;
import ServerClasses.Service.Services.RegisterService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class RegisterRequestHandler extends PostRequestHandler {

    public Response getResponse(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        System.out.println(reqData);

        RegisterRequest r = JsonSerializer.deserialize(reqData, RegisterRequest.class);
        return RegisterService.register(r);
    }
}