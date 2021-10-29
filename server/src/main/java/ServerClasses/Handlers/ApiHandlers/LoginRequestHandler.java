package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.PostRequestHandler;
import Request.LoginRequest;
import Result.Response;
import ServerClasses.Service.ServiceTools.JsonSerializer;
import ServerClasses.Service.Services.LoginService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class LoginRequestHandler extends PostRequestHandler {

    protected Response getResponse(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        System.out.println(reqData);

        LoginRequest l = JsonSerializer.deserialize(reqData, LoginRequest.class);
        return LoginService.login(l);
    }
}

