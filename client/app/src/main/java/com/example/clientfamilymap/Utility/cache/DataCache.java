package com.example.clientfamilymap.Utility.cache;

import com.example.clientfamilymap.Utility.ServerProxy;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Model.AuthToken;
import Model.Event;
import Model.Person;
import Request.AllEventServiceRequest;
import Request.AllPersonServiceRequest;
import Result.Responses.AllEventServiceResult;
import Result.Responses.AllPersonServiceResult;

public class DataCache {

//Members -------------------------------------------------

    private static DataCache instance;

    private AuthToken authToken;
    private final Map<String, Person> peopleById = new HashMap<>();
    private final Map<String, SortedSet<Event>> eventsByPersonId = new HashMap<>();

    private final Comparator<Event> eventComparator = (event1, event2) -> {
        String event1Type = event1.getEventType().toLowerCase();
        String event2Type = event2.getEventType().toLowerCase();

        //sort birth first, death last
        switch(event1Type) {
            case("birth"):
                return -1;
            case("death"):
                return 1;
        }
        switch(event2Type) {
            case("birth"):
                return 1;
            case("death"):
                return -1;
        }

        //sort anything else by year
        int event1Year = event1.getYear();
        int event2Year = event2.getYear();

        int compare = event1Year - event2Year;

        //sort same years alphabetically
        if (compare == 0) {
            event1Type = event1.getEventType();
            event2Type = event2.getEventType();

            compare = event1Type.compareTo(event2Type);
        }

        return compare;
    };

    //User Person object
    private Person userPerson;

    //Immediate Family
    private Person spouse;

    //Ancestors
    private final Set<Person> fatherSideMales = new HashSet<>();
    private final Set<Person> fatherSideFemales = new HashSet<>();
    private final Set<Person> motherSideMales = new HashSet<>();
    private final Set<Person> motherSideFemales = new HashSet<>();

    //Singleton architecture --------------------------------------------

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache() {

    }

    //DataCache fillers --------------------------------------------------------

    public void populate(String personId) {
        assert authToken != null;

        dataWipe();

        populatePeopleMap();
        populateEventsMap();

        setUserPerson(peopleById.get(personId));

        if (!peopleById.isEmpty()) {
            this.spouse = peopleById.get(userPerson.getSpouseID());
            sortMotherSide(peopleById.get(userPerson.getMotherID()));
            sortFatherSide(peopleById.get(userPerson.getFatherID()));
        }
    }

    private void populatePeopleMap() {
        AllPersonServiceRequest peopleRequest = new AllPersonServiceRequest(authToken.getAuthtoken());
        AllPersonServiceResult peopleResult = ServerProxy.getPeople(peopleRequest);

        if (peopleResult.isSuccess()) {
            for (Person person : peopleResult.getData()) {
                peopleById.put(person.getPersonID(), person);
            }
        }
        else {
            System.out.println("Populate people unsuccessful - request failure");
        }
    }

    private void populateEventsMap() {
        AllEventServiceRequest eventsRequest = new AllEventServiceRequest(authToken.getAuthtoken());
        AllEventServiceResult eventsResult = ServerProxy.getEvents(eventsRequest);

        if (eventsResult.isSuccess()) {
            for (Event event : eventsResult.getData()) {
                SortedSet<Event> eventSet = eventsByPersonId.get(event.getPersonID());

                if (eventSet == null) {
                    eventSet = new TreeSet<>(eventComparator);
                }

                eventSet.add(event);
                eventsByPersonId.put(event.getPersonID(), eventSet);
            }
        }
        else {
            System.out.println("Populate people unsuccessful - request failure");
        }
    }

    private void sortFatherSide(Person person) {
        assert person != null;

        if (person.getGender().equals("m")) {
            fatherSideMales.add(person);
        }
        else {
            fatherSideFemales.add(person);
        }

        String fatherId = person.getFatherID();
        if (fatherId != null) {
            sortFatherSide(peopleById.get(fatherId));
        }
        String motherId = person.getMotherID();
        if (motherId != null) {
            sortFatherSide(peopleById.get(motherId));
        }
    }

    private void sortMotherSide(Person person) {
        assert person != null;

        if (person.getGender().equals("m")) {
            motherSideMales.add(person);
        }
        else {
            motherSideFemales.add(person);
        }

        String fatherId = person.getFatherID();
        if (fatherId != null) {
            sortMotherSide(peopleById.get(fatherId));
        }
        String motherId = person.getMotherID();
        if (motherId != null) {
            sortMotherSide(peopleById.get(motherId));
        }
    }

    private void dataWipe() {
        spouse = null;
        peopleById.clear();
        eventsByPersonId.clear();
        motherSideMales.clear();
        motherSideFemales.clear();
        fatherSideFemales.clear();
        fatherSideMales.clear();
    }

    public void logout() {
        dataWipe();
        authToken = null;
        userPerson = null;
        spouse = null;
    }

    //Setters ----------------------------------------------------------------

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setUserPerson(Person userPerson) {
        this.userPerson = userPerson;
    }


    //Getters ----------------------------------------------------------------

    public AuthToken getAuthToken() {
        return authToken;
    }

    public Map<String, Person> getPeopleById() {
        return peopleById;
    }

    public Map<String, SortedSet<Event>> getEventsByPersonID() {
        return eventsByPersonId;
    }

    public Person getUserPerson() {
        return userPerson;
    }

    public Person getSpouse() {
        return spouse;
    }

    public Set<Person> getFatherSideMales() {
        return fatherSideMales;
    }

    public Set<Person> getFatherSideFemales() {
        return fatherSideFemales;
    }

    public Set<Person> getMotherSideMales() {
        return motherSideMales;
    }

    public Set<Person> getMotherSideFemales() {
        return motherSideFemales;
    }
}
