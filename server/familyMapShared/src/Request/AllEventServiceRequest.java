package Request;

/**
 * All Events API Requests
 */
public class AllEventServiceRequest {

    /**
     * stores required auth token
     */
    private String authtoken;

    public AllEventServiceRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

}
