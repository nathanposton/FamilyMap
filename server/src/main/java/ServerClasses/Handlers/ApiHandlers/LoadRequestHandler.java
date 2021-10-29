package ServerClasses.Handlers.ApiHandlers;

import ServerClasses.Handlers.PostRequestHandler;
import Request.LoadRequest;
import Result.Response;
import ServerClasses.Service.ServiceTools.JsonSerializer;
import ServerClasses.Service.Services.LoadService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class LoadRequestHandler extends PostRequestHandler {


    public Response getResponse(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        System.out.println(reqData);

        LoadRequest l = JsonSerializer.deserialize(reqData, LoadRequest.class);
        return LoadService.load(l);
    }
}
