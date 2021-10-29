package Request;

/**
 * Register API Requests
 */
public class RegisterRequest {
    /**
     * stores request body username parameter
     */
    private String username;
    /**
     * stores request body password parameter
     */
    private String password;
    /**
     * stores request body email parameter
     */
    private String email;
    /**
     * stores request body firstName parameter
     */
    private String firstName;
    /**
     * stores request body lastName parameter
     */
    private String lastName;
    /**
     * stores request body gender parameter
     */
    private String gender;

    public RegisterRequest() {

    }

    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
