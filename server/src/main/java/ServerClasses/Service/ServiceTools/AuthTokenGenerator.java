package ServerClasses.Service.ServiceTools;

import java.util.UUID;

public class AuthTokenGenerator {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
