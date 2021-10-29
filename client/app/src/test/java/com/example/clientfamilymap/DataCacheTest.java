package com.example.clientfamilymap;

import com.example.clientfamilymap.Utility.ServerProxy;
import com.example.clientfamilymap.Utility.cache.DataCache;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import Model.AuthToken;
import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.Responses.LoginResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataCacheTest {

    //Members --------------------------------------------------------------------------------------

    private Person sheila;
    private Person sheilaMother;
    private Person sheilaFather;
    private Person fatherFather;
    private Person fatherMother;
    private Person motherFather;
    private Person motherMother;

    private Event sheilaBirth;
    private Event sheilaDeath;

    //Setup ----------------------------------------------------------------------------------------

    @Before
    public void setUp() {
        sheila = new Person("Sheila_Parker", "sheila",
                "Sheila", "Parker", "f",
                "Blaine_McGary",
                "Betty_White", "Davis_Hyer");

        sheilaFather = new Person("Blaine_McGary", "sheila",
                "Blaine", "McGary", "m",
                "Ken_Rodham",
                "Mrs_Rodham",
                "Betty_White");

        fatherFather = new Person("Ken_Rodham", "sheila",
                "Ken", "Rodham", "m",
                null,
                null,
                "Mrs_Rodham");

        fatherMother = new Person("Mrs_Rodham", "sheila",
                "Mrs", "Rodham", "f",
                null,
                null,
                "Ken_Rodham");

        sheilaMother = new Person("Betty_White", "sheila",
                "Betty", "White", "f",
                "Frank_Jones",
                "Mrs_Jones",
                "Blaine_McGary");

        motherFather = new Person("Frank_Jones", "sheila",
                "Frank", "Jones", "m",
                null,
                null,
                "Mrs_Jones");

        motherMother = new Person("Mrs_Jones", "sheila",
                "Mrs", "Jones", "f",
                null,
                null,
                "Frank_Jones");

        sheilaBirth = new Event("Sheila_Birth", "sheila", "Sheila_Parker",
                -36.1833000183105f, 144.966705322266f,
                "Australia", "Melbourne", "birth", 1970);

        sheilaDeath = new Event("Sheila_Death", "sheila", "Sheila_Parker",
                                40.2444000244141f, 111.660797119141f,
                        "United States", "Provo", "death", 2015);
    }

    //Tests ----------------------------------------------------------------------------------------

    @Test
    public void peopleSortPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));
    }

    @Test
    public void peopleSortFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getPeopleById().isEmpty());
    }

    @Test
    public void eventSortPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));

        assertEquals(sheilaBirth, dataCache.getEventsByPersonID().get(sheila.getPersonID()).first());
        assertEquals(sheilaDeath, dataCache.getEventsByPersonID().get(sheila.getPersonID()).last());
    }

    @Test
    public void eventSortFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getEventsByPersonID().isEmpty());
    }

    @Test
    public void motherSideFemalesPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));

        assertTrue(setContainsPerson(sheilaMother, dataCache.getMotherSideFemales()));
        assertTrue(setContainsPerson(motherMother, dataCache.getMotherSideFemales()));

    }

    @Test
    public void motherSideFemalesFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getMotherSideFemales().isEmpty());

    }

    @Test
    public void motherSideMalesPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));

        assertTrue(setContainsPerson(motherFather, dataCache.getMotherSideMales()));

    }

    @Test
    public void motherSideMalesFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getMotherSideMales().isEmpty());

    }

    @Test
    public void fatherSideMalesPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));

        assertTrue(setContainsPerson(sheilaFather, dataCache.getFatherSideMales()));
        assertTrue(setContainsPerson(fatherFather, dataCache.getFatherSideMales()));
    }

    @Test
    public void fatherSideMalesFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getFatherSideMales().isEmpty());

    }

    @Test
    public void fatherSideFemalesPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = ServerProxy.login(loginRequest);

        AuthToken authToken = new AuthToken(loginResult.getAuthtoken(), loginRequest.getUsername());
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
        dataCache.populate(sheila.getPersonID());

        assertEquals(sheila.getFatherID(), dataCache.getPeopleById().get(sheila.getFatherID()).getPersonID());
        assertTrue(dataCache.getPeopleById().containsValue(sheilaFather));

        assertTrue(setContainsPerson(fatherMother, dataCache.getFatherSideFemales()));
        assertTrue(setContainsPerson(sheilaMother, dataCache.getMotherSideFemales()));

    }

    @Test
    public void fatherSideFemalesFail() {
        AuthToken authToken = new AuthToken("notAnAuthToken", "sheila");

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);

        dataCache.populate(sheila.getPersonID());

        assertNotNull(dataCache);
        assertTrue(dataCache.getFatherSideFemales().isEmpty());

    }

    //Helpers --------------------------------------------------------------------------------------

    private boolean setContainsPerson(Person person, Set<Person> personSet) {
        boolean contains = false;
        for (Person dataPerson : personSet) {
            if (dataPerson.equals(person)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
