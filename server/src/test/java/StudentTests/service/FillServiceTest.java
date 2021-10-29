package StudentTests.service;

import ServerClasses.DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Request.RegisterRequest;
import Result.Responses.LoadResult;
import Result.Responses.FillResult;
import Result.Responses.RegisterResult;
import ServerClasses.Service.ServiceTools.AuthTokenGenerator;
import ServerClasses.Service.Services.LoadService;
import ServerClasses.Service.Services.FillService;
import ServerClasses.Service.Services.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database db;

    //    private AuthTokenDao authTokenDao;
    private AuthToken bestAuthToken;
    private AuthToken relatedAuthToken;
    private AuthToken aToken;
    private AuthToken bToken;
    private AuthToken cToken;
    private AuthToken dToken;

    //    private UserDao userDao;
    private User bestUser;
    private User relatedUser;

    //    private PersonDao personDao;
    private Person bestPerson;
    private Person relatedPerson;
    private Person child;
    private Person spouse;
    private Person father;
    private Person mother;
    private Person fatherMother;
    private Person fatherFather;
    private Person motherMother;
    private Person motherFather;

    //    private EventDao eventDao;
    private Event bestEvent;
    private Event anotherEvent;
    private Event aEvent;
    private Event aaEvent;
    private Event bEvent;
    private Event bbEvent;

    @BeforeEach
    public void setUp() throws DataAccessException {
//        db = new Database();
//        Connection conn = db.getConnection();

//        db.clearTables();

        /*
        authTokenDao = new AuthTokenDao(conn);
        userDao = new UserDao(conn);
        personDao = new PersonDao(conn);
        eventDao = new EventDao(conn);

         */

        //AuthToken
        bestAuthToken = new AuthToken("authtoken123", "username234");
        relatedAuthToken = new AuthToken("ANOTHERauthtoken123", "ANOTHERusername234");
        aToken = new AuthToken("ERauthtoken123", "alphabet");
        bToken = new AuthToken("oken123", "alphabet");
        cToken = new AuthToken("n123", "alphabet");
        dToken = new AuthToken("OTHERauthtoken123", "ANOTHERusername234");

        //User
        bestUser = new User("user3name", "pas2wsword", "emailaddress",
                "firstname", "lastname", "f", "IDafdsfasd342");
        relatedUser = new User("ANOTHERuser3name", "ANOTHERpas2wsword", "ANOTHERemailaddress",
                "ANOTHERfirstname", "ANOTHERlastname", "m", "anotherIDafdsfasd342");

        //Person
        bestPerson = new Person("imaperson4ID", "imausername", "imafirstname",
                "imalastname", "f", null, null, null);
        relatedPerson = new Person("relatedP3ersonID", "relatedUsern4me", "related",
                "Person", "m", "f4ther", "m0ther", "sp0use;");
        child = new Person("childID", "childUsername", "childFirstName",
                "spouseLastName", "m", "fatherID", "motherID", "spouseID");
        spouse = new Person("spouseID", "spouseUsername", "spouseFirstName",
                "spouseLastName", "f", null, null, "fatherID");
        father = new Person("fatherID", "childUsername", "fatherFirstName",
                "fatherLastName", "m", "fatherFatherID", "fatherMotherID", "motherID");
        mother = new Person("motherID", "childUsername", "motherFirstName",
                "motherLastName", "f", "motherFatherID", "motherMotherID", "fatherID");
        fatherMother = new Person("fatherMotherID", "childUsername", "fatherMotherFirstName",
                "fatherMotherLastName", "f", null, null, "fatherFatherID");
        fatherFather = new Person("fatherFatherID", "childUsername", "fatherFatherFirstName",
                "fatherFatherLastName", "m", null, null, "fatherMotherID");
        motherMother = new Person("motherMotherID", "childUsername", "motherMotherFirstName",
                "motherMotherLastName", "f", null, null, "motherFatherID");
        motherFather = new Person("motherFatherID", "childUsername", "motherFatherFirstName",
                "motherFatherLastName", "m", null, null, "motherMotherID");

        //Event
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        anotherEvent = new Event("another23event", "eventUser12", "Guy213",
                32.9f, 0.0f, "New Zealand", "Quoka",
                "Festival", 2666);
        aEvent = new Event("aEventID", "aEventUser", "aEventPersonID",
                32.9f, 0.0f, "New Zealand", "Quoka",
                "aEventType", 2666);
        aaEvent = new Event("aaEventID", "aEventUser", "aEventPersonID",
                32.9f, 0.0f, "New Zealand", "Quoka",
                "aEventType", 2666);
        bEvent = new Event("bEventID", "bEventUser", "bEventPersonID",
                32.9f, 0.0f, "New Zealand", "Quoka",
                "bEventType", 2666);
        bbEvent = new Event("bbEventID", "bEventUser", "bEventPersonID",
                32.9f, 0.0f, "New Zealand", "Quoka",
                "bEventType", 2666);

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
//        db.closeConnection(false);
    }

    @Test
    public void servicePass() throws DataAccessException {
        //generating user to fill
        String randomUsername = AuthTokenGenerator.generate();
        RegisterRequest registerRequest = new RegisterRequest(randomUsername, bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());

        RegisterResult registerResult = RegisterService.register(registerRequest);

        //filling user
        FillRequest request = new FillRequest(registerRequest.getUsername(), 4);

        FillResult result = FillService.fill(request);

        assertEquals("Successfully added 31 persons and 93 events to the database.", result.getMessage());
        assertTrue(result.isSuccess());
    }

    @Test
    public void serviceFail() throws DataAccessException {
        LoadResult result = LoadService.load(null);

        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());

    }



}
