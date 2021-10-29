package Model;

/**
 * Person POJO
 */
public class Person {

    /**
     * Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Person’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Person's gender (String: 'f' or 'm')
     */
    private String gender;
    /**
     * Person ID of person’s father (possibly null)
     */
    private String fatherID;
    /**
     * Person ID of person’s mother (possibly null)
     */
    private String motherID;
    /**
     * Person ID of person’s spouse (possibly null)
     */
    private String spouseID;

    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;

    }
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;

            if ( ( (oPerson.getMotherID() == null) != (getMotherID() == null) ) ||
                 ( (oPerson.getFatherID() == null) != (getFatherID() == null) ) ||
                 ( (oPerson.getSpouseID() == null) != (getSpouseID() == null) ) ) {
                return false;
            }
            if ( (oPerson.getMotherID() != null && !(oPerson.getMotherID().equals(getMotherID())) ) ||
                 (oPerson.getFatherID() != null && !(oPerson.getFatherID().equals(getFatherID())) ) ||
                 (oPerson.getSpouseID() != null && !(oPerson.getSpouseID().equals(getSpouseID())) ) ) {
                return false;
            }

            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender());
        } else {
            return false;
        }
    }
}
