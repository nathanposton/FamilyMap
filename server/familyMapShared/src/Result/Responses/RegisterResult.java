package Result.Responses;

import Result.Response;

/**
 * Register API Results
 */
public class RegisterResult extends Response {
    /**
     * stores retrieved auth token
     */
    private String authtoken;
    /**
     * stores retrieved username
     */
    private String username;
    /**
     * stores retrieved personID
     */
    private String personID;

    public RegisterResult(Exception e) {
        super(e);
    }

    public RegisterResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = true;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}