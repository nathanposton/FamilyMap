package com.example.clientfamilymap.Utility.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Model.Event;
import Model.Person;

public class SearchCache {

    //Members --------------------------------------------------------------------------------------

    private static SearchCache instance;

    private final List<Person> personList = new ArrayList<>();
    private final List<Event> eventList = new ArrayList<>();

    private final List<Person> refinedPersonList = new ArrayList<>();
    private final List<Event> refinedEventList = new ArrayList<>();
    private final List<Person> children = new ArrayList<>();

    private static final String FEMALE = "f";
    private final DataCache dataCache = DataCache.getInstance();
    private final SettingsCache settingsCache = SettingsCache.getInstance();

    //Singleton Architecture -----------------------------------------------------------------------

    public static SearchCache getInstance() {
        if (instance == null) {
            instance = new SearchCache();
        }

        return instance;
    }
    private SearchCache() {

    }

    //Initialize -----------------------------------------------------------------------------------

    public void populate() {
        if (personList.isEmpty()) {
            for (Person person : dataCache.getPeopleById().values()) {
                if (person != null) {
                    this.personList.add(person);

                    for (Event event : dataCache.getEventsByPersonID().get(person.getPersonID())) {
                        if (event != null) {
                            eventList.add(event);
                        }
                    }
                }
            }
        }
    }

    //RecyclerView ---------------------------------------------------------------------------------

    public List<Person> getRefinedPersonList(String query) {
        refinedPersonList.clear();

        if (query != null) {
            String searchTerm = query.toLowerCase();

            if (!searchTerm.isEmpty()) {
                for (Person person : personList) {
                    if (person.getFirstName().toLowerCase().contains(searchTerm) ||
                            person.getLastName().toLowerCase().contains(searchTerm)) {

                        refinedPersonList.add(person);
                    }
                }
            }
        }

        return refinedPersonList;
    }

    public List<Event> getRefinedEventList(String query) {
        refinedEventList.clear();

        if (query != null) {
            String searchTerm = query.toLowerCase();

            Set<Person> filteredPeople = settingsCache.getIncludedPeople();

            if (!searchTerm.isEmpty()) {
                for (Event event : eventList) {
                    Person person = dataCache.getPeopleById().get(event.getPersonID());
                    assert person != null;

                    if(!filteredPeople.contains(person)) {
                        continue;
                    }

                    if (event.getEventType().toLowerCase().contains(searchTerm) ||
                            event.getCity().toLowerCase().contains(searchTerm) ||
                            event.getCountry().toLowerCase().contains(searchTerm) ||
                            String.valueOf(event.getYear()).toLowerCase().contains(searchTerm)) {

                        refinedEventList.add(event);
                    }
                }
            }
        }

        return refinedEventList;
    }

    //ExpandableList -------------------------------------------------------------------------------

    public List<Person> getChildren(Person mainPerson) {
        children.clear();

        if (mainPerson.getGender().equals(FEMALE)) {
            for (Person person : personList) {
                String motherId = person.getMotherID();
                if (motherId != null && motherId.equals(mainPerson.getPersonID())) {
                    children.add(person);
                }
            }
        }
        else {
            for (Person person : personList) {
                String fatherId = person.getFatherID();
                if (fatherId != null && fatherId.equals(mainPerson.getPersonID())) {
                    children.add(person);
                }
            }
        }
        return children;
    }

    public List<Person> getRefinedPersonList(Person mainPerson) {
        refinedPersonList.clear();

        String fatherId = mainPerson.getFatherID();
        String motherId = mainPerson.getMotherID();
        String spouseId = mainPerson.getSpouseID();

        Person father = null;
        if (fatherId != null) {
            father = dataCache.getPeopleById().get(mainPerson.getFatherID());
        }

        Person mother = null;
        if (motherId != null) {
            mother = dataCache.getPeopleById().get(mainPerson.getMotherID());
        }

        Person spouse = null;
        if (spouseId != null) {
            spouse = dataCache.getPeopleById().get(mainPerson.getSpouseID());
        }

        refinedPersonList.add(father);
        refinedPersonList.add(mother);
        refinedPersonList.add(spouse);

        refinedPersonList.addAll(getChildren(mainPerson));

        return refinedPersonList;
    }

    public List<Event> getRefinedEventList(Person mainPerson) {
        refinedEventList.clear();

        for (Event event : eventList) {
            if (event.getPersonID().equals(mainPerson.getPersonID())) {
                refinedEventList.add(event);
            }
        }

        return refinedEventList;
    }

    //Logout ---------------------------------------------------------------------------------------

    public void logout() {
        personList.clear();
        eventList.clear();

        refinedPersonList.clear();
        refinedEventList.clear();
        children.clear();
    }

    //Getters Setters ------------------------------------------------------------------------------

    public List<Person> getPersonList() {
        return personList;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
