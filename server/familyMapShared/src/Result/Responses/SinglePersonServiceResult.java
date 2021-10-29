package Result.Responses;

import Result.Response;

/**
 * Single Person API Results
 */
public class SinglePersonServiceResult extends Response {
    private String associatedUsername;
    /**
     * stores retrieved personID
     */
    private String personID;
    /**
     * stores retrieved firstName
     */
    private String firstName;
    /**
     * stores retrieved lastName
     */
    private String lastName;
    /**
     * stores retrieved gender
     */
    private String gender;
    /**
     * stores retrieved fatherID
     */
    private String fatherID;
    /**
     * stores retrieved motherID
     */
    private String motherID;
    /**
     * stores retrieved spouseID message
     */
    private String spouseID;

    public SinglePersonServiceResult(Exception ex) {
        super(ex);
    }

    public SinglePersonServiceResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = true;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
