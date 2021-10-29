package Request;

/**
 * All Persons API Requests
 */
public class AllPersonServiceRequest {
    public AllPersonServiceRequest(String authToken) {
        this.authtoken = authToken;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * stores required auth token
     */
    private String authtoken;
}
