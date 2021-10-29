package ServerClasses.Service.ServiceTools;

import ServerClasses.DataAccess.DataAccessException;
import ServerClasses.DataAccess.EventDao;
import ServerClasses.DataAccess.PersonDao;
import Model.Event;
import Model.Person;
import Model.User;
import ServerClasses.Service.ServiceTools.GenerationFillerTools.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerationFiller {
    private final PersonDao personDao;
    private final EventDao eventDao;
    private final User user;
    private int usersAdded = 0;
    private int eventsAdded = 0;

    public GenerationFiller(PersonDao personDao, EventDao eventDao, User user) {
        this.personDao = personDao;
        this.eventDao = eventDao;
        this.user = user;
    }

    public void cleanUser() throws DataAccessException {
        personDao.clearByUsername(user.getUsername());
        eventDao.clear(user.getUsername());
    }

    public void doFill(int generations) throws IOException, DataAccessException {
        if (generations < 0) {
            generations = 4;
        }
        int calcDate = 1990;

        Person userPerson = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getGender(), null, null, null);

        recurseGenerateParent(userPerson, generations, calcDate);
    }


    private void recurseGenerateParent(Person child, int generations, int calcDate) throws DataAccessException, IOException {


        if (generations != 0) {
            Person father = generatePerson("m");
            Person mother = generatePerson("f");

            father.setLastName(child.getLastName());
            father.setSpouseID(mother.getPersonID());
            mother.setSpouseID(father.getPersonID());


            recurseGenerateParent(father, generations - 1, calcDate - 27);
            recurseGenerateParent(mother, generations - 1, calcDate - 27);

            child.setFatherID(father.getPersonID());
            child.setMotherID(mother.getPersonID());
        }

        personDao.insert(child);
        usersAdded++;
        generateEventsForOnePerson(child, calcDate);
    }


    private Person generatePerson(String gender) throws IOException {

        String personID = AuthTokenGenerator.generate();
        String firstName = null;
        if (gender.equals("m")) {
            firstName = generateRandomName(getMNames());
        }
        else if (gender.equals("f")) {
            firstName = generateRandomName(getFNames());
        }
        String lastName = generateRandomName(getSNames());

        return new Person(personID, user.getUsername(), firstName, lastName, gender, null, null, null);
    }

    private String generateRandomName(List<String> names) {
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }

    private void generateEventsForOnePerson(Person person, int calcDate) throws DataAccessException, IOException {
        Event birth = generateLifeEvent(person, "birth", calcDate);

        Event marriage = generateLifeEvent(person, "marriage", calcDate + 20);
        Person spouse = personDao.findOne(person.getSpouseID());
        matchEvent(spouse, marriage);

        Event death = generateLifeEvent(person, "death", calcDate + 50);

        eventDao.insert(birth);
        eventDao.insert(marriage);
        eventDao.insert(death);
    }

    private void matchEvent(Person spouse, Event event) throws DataAccessException {
        if (spouse != null) {
            ArrayList<Event> spouseEvents = eventDao.findTypeByPerson(spouse.getPersonID(), event.getEventType());
            if (spouseEvents != null) {
                eventDao.clearPersonEventByType(spouse.getPersonID(), event.getEventType());
            }
            Event spouseEvent = new Event(AuthTokenGenerator.generate(), event.getAssociatedUsername(),
                                          spouse.getPersonID(), event.getLatitude(), event.getLongitude(),
                                          event.getCountry(), event.getCity(), event.getEventType(),
                                          event.getYear());
            eventDao.insert(spouseEvent);
        }
    }


    private Event generateLifeEvent(Person original, String eventType, int calcDate) throws IOException {
        String eventID = AuthTokenGenerator.generate();
        String associatedUsername = original.getAssociatedUsername();
        String personID = original.getPersonID();

        List<Location> locations = getLocations();
        Random rand = new Random();
        Location randomLocation = locations.get(rand.nextInt(locations.size()));
        float latitude = randomLocation.getLatitude();
        float longitude = randomLocation.getLongitude();
        String country = randomLocation.getCountry();
        String city = randomLocation.getCity();

        int year = calcDate;

        eventsAdded++;

        return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city,
                eventType, year);
    }

    // Functions to retrieve JSON objects -----------------------------

    private List<String> getMNames() throws IOException {
        File file = new File("json/mnames.json");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            MaleNameData maleNameData = gson.fromJson(bufferedReader, MaleNameData.class);

            return maleNameData.getData();
        }
    }

    private List<String> getFNames() throws IOException {
        File file = new File("json/fnames.json");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            FemaleNameData femaleNameData = gson.fromJson(bufferedReader, FemaleNameData.class);

            return femaleNameData.getData();
        }
    }

    private List<String> getSNames() throws IOException {
        File file = new File("json/snames.json");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            SurnameData surnameData = gson.fromJson(bufferedReader, SurnameData.class);

            return surnameData.getData();
        }
    }

    private List<Location> getLocations() throws IOException {
        File file = new File("json/locations.json");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            LocationData locationData = gson.fromJson(bufferedReader, LocationData.class);

            return locationData.getData();
        }
    }


    //Getters & Setters -------------------------

    public int getUsersAdded() {
        return usersAdded;
    }

    public void setUsersAdded(int usersAdded) {
        this.usersAdded = usersAdded;
    }

    public int getEventsAdded() {
        return eventsAdded;
    }

    public void setEventsAdded(int eventsAdded) {
        this.eventsAdded = eventsAdded;
    }
}