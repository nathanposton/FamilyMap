package Request;

/**
 * Single Person API Requests
 */
public class SinglePersonServiceRequest {
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public SinglePersonServiceRequest(String personID, String authToken) {
        this.personID = personID;
        this.authtoken = authToken;
    }

    /**
     * stores request body personID parameter
     */
    private String personID;
    /**
     * stores required auth token
     */
    private String authtoken;
}
