package Model;

/**
 * Authorization Tokens POJO
 */
public class AuthToken {
    /**
     * stores token associated with a logged in user
     */
    private String authtoken;
    /**
     * stores the username the token was made for
     */
    private String username;

    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthtoken().equals(getAuthtoken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        } else {
            return false;
        }
    }
}
