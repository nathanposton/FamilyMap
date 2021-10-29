package ServerClasses.Service.ServiceTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
    public static String serialize(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(o);
    }

}
