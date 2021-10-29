package Model;

/**
 * User POJO
 */
public class User {

    /**
     * Unique username (non-empty string)
     */
    private String username;
    /**
     * User’s password (non-empty string)
     */
    private String password;
    /**
     * User’s email address (non-empty string)
     */
    private String email;
    /**
     * User’s first name (non-empty string)
     */
    private String firstName;
    /**
     * User’s last name (non-empty string)
     */
    private String lastName;
    /**
     * User’s gender (String: 'f' or 'm')
     */
    private String gender;
    /**
     * Unique Person ID assigned to this user’s generated Person object (non-empty string)
     */
    private String personID;

    public User(String username, String password, String email, String firstName, String lastName,
                String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
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

    public String getPersonID() {
        return personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
