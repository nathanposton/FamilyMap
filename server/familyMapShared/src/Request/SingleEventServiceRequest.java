package Request;

/**
 * Single Event API Requests
 */
public class SingleEventServiceRequest {
    /**
     * stores request body eventID parameter
     */
    private String eventID;
    /**
     * stores required auth token
     */
    private String authtoken;

    public SingleEventServiceRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authtoken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
