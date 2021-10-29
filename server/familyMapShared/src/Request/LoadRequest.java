package Request;

import Model.Event;
import Model.Person;
import Model.User;
import Result.Response;

import java.util.List;

/**
 * Load API Requests
 */
public class LoadRequest extends Response {
    /**
     * stores request body Array of User objects parameter
     */
    private List<User> users;
    /**
     * stores request body Array of Person objects parameter
     */
    private List<Person> persons;
    /**
     * stores request body Array of Events objects parameter
     */
    private List<Event> events;

    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
        this.success = true;
    }

    public LoadRequest(Exception ex) {
        super(ex);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
