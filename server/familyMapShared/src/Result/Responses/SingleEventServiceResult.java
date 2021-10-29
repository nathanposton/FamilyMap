package Result.Responses;

import Result.Response;

/**
 * Single Event API Results
 */
public class SingleEventServiceResult extends Response {

    private String associatedUsername;
    /**
     * stores retrieved eventID
     */
    private String eventID;

    /**
     * stores retrieved personID
     */
    private String personID;

    /**
     * stores retrieved latitude
     */
    private float latitude;
    /**
     * stores retrieved longitude
     */
    private float longitude;
    /**
     * stores retrieved country
     */
    private String country;
    /**
     * stores retrieved city
     */
    private String city;
    /**
     * stores retrieved eventType
     */
    private String eventType;
    /**
     * stores retrieved year message
     */
    private int year;

    public SingleEventServiceResult(String eventID, String associatedUsername, String personID, float latitude,
                                    float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = true;
    }

    public SingleEventServiceResult(Exception ex) {
        super(ex);
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
