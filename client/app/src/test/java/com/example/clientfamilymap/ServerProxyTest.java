package com.example.clientfamilymap;

import org.junit.Before;
import org.junit.Test;

import Model.Event;
import Model.Person;
import Request.AllEventServiceRequest;
import Request.AllPersonServiceRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.Responses.AllEventServiceResult;
import Result.Responses.AllPersonServiceResult;
import Result.Responses.LoginResult;
import Result.Responses.RegisterResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.clientfamilymap.Utility.ServerProxy;

public class ServerProxyTest {

    //Members --------------------------------------------------------------------------------------

    private Person sheila;
    private Person sheilaFather;
    private Person sheilaMother;
    private Person motherFather;
    private Person motherMother;
    private Person fatherMother;
    private Person fatherFather;

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
    public void loginPass() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");

        LoginResult loginResult = ServerProxy.login(loginRequest);

        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
        assertNotNull(loginResult.getAuthtoken());
        assertNotNull(loginResult.getPersonID());
        assertEquals("sheila", loginRequest.getUsername());
    }

    @Test
    public void loginFail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("notA");
        loginRequest.setPassword("User");

        LoginResult loginResult = ServerProxy.login(loginRequest);

        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
        assertNull(loginResult.getAuthtoken());
        assertNotNull(loginResult.getMessage());
    }

    @Test
    public void registerPass() {
        //FIXME: currently must be changed to unused username each time
        String username = "newUser68";
        RegisterRequest registerRequest = new RegisterRequest(username, "password",
                                                              "email@email.com", "firstName",
                                                              "lastName", "m");

        RegisterResult registerResult = ServerProxy.register(registerRequest);

        assertNotNull(registerResult);
        assertTrue(registerResult.isSuccess());
        assertNotNull(registerResult.getAuthtoken());
        assertNotNull(registerResult.getPersonID());
        assertEquals(username, registerResult.getUsername());
    }

    @Test
    public void registerFail() {
        String username = "newUser5";
        RegisterRequest registerRequest = new RegisterRequest(username, "password",
                "email@email.com", "firstName",
                "lastName", "m");

        ServerProxy.register(registerRequest);
        //user already registered -> returns error
        RegisterResult registerResult = ServerProxy.register(registerRequest);

        assertFalse(registerResult.isSuccess());
        assertNull(registerResult.getAuthtoken());
        assertNull(registerResult.getPersonID());
        assertNull(registerResult.getUsername());
        assertNotNull(registerResult.getMessage());
    }

    @Test
    public void getEventsPass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");

        LoginResult loginResult = ServerProxy.login(loginRequest);

        AllEventServiceRequest allEventServiceRequest = new AllEventServiceRequest(loginResult.getAuthtoken());

        AllEventServiceResult allEventServiceResult = ServerProxy.getEvents(allEventServiceRequest);

        assertNotNull(allEventServiceResult);
        assertTrue(allEventServiceResult.isSuccess());
        assertNotNull(allEventServiceResult.getData());

        assertTrue(allEventServiceResult.getData().contains(sheilaBirth));
    }

    @Test
    public void getEventsFail() {
        String badAuthToken = "notAnAuthToken";

        AllEventServiceRequest allEventServiceRequest = new AllEventServiceRequest(badAuthToken);

        AllEventServiceResult allEventServiceResult = ServerProxy.getEvents(allEventServiceRequest);

        assertNotNull(allEventServiceResult);
        assertFalse(allEventServiceResult.isSuccess());
        assertNull(allEventServiceResult.getData());
        assertNotNull(allEventServiceResult.getMessage());
    }

    @Test
    public void getPeoplePass() {
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");

        LoginResult loginResult = ServerProxy.login(loginRequest);

        AllPersonServiceRequest allPersonServiceRequest = new AllPersonServiceRequest(loginResult.getAuthtoken());

        AllPersonServiceResult allPersonServiceResult = ServerProxy.getPeople(allPersonServiceRequest);

        assertNotNull(allPersonServiceResult);
        assertTrue(allPersonServiceResult.isSuccess());
        assertNotNull(allPersonServiceResult.getData());

        //FIXME - success depends on existence of specific event
        assertTrue(allPersonServiceResult.getData().contains(sheila));
    }

    @Test
    public void getPeopleFail() {
        String badAuthToken = "notAnAuthToken";

        AllPersonServiceRequest allPersonServiceRequest = new AllPersonServiceRequest(badAuthToken);

        AllPersonServiceResult allPersonServiceResult = ServerProxy.getPeople(allPersonServiceRequest);

        assertNotNull(allPersonServiceResult);
        assertFalse(allPersonServiceResult.isSuccess());
        assertNull(allPersonServiceResult.getData());
        assertNotNull(allPersonServiceResult.getMessage());
    }
}