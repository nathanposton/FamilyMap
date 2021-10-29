package StudentTests.DaoTests;

import ServerClasses.DataAccess.DataAccessException;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private Event anotherEvent;
    private Event aEvent;
    private Event aaEvent;
    private Event bEvent;
    private Event bbEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
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
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDao so it can access the database
        eDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        eDao.insert(bestEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(bestEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);

        eDao.insert(anotherEvent);
        compareTest = eDao.find(anotherEvent.getEventID());

        assertNotNull(compareTest);
        assertEquals(anotherEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(bestEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }


    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(anotherEvent);

        //Test find
        Event compareTest = eDao.find(bestEvent.getEventID());
        Event compareTest1 = eDao.find(anotherEvent.getEventID());

        assertNotNull(compareTest);
        assertNotNull(compareTest1);

        assertEquals(bestEvent, compareTest);
        assertEquals(anotherEvent, compareTest1);

        //Test findAll
        ArrayList<Event> compareAllTest = eDao.findAll();

        assertNotNull(compareAllTest);

        assertEquals(bestEvent, compareAllTest.get(0));
        assertEquals(anotherEvent, compareAllTest.get(1));
    }

    @Test
    public void findFail() throws DataAccessException {
        assertNull(eDao.findAll());

        eDao.insert(bestEvent);
        assertNull(eDao.find(anotherEvent.getEventID()));
    }

    @Test
    public void findTypeByPersonPass() throws DataAccessException {
        eDao.insert(aEvent);
        eDao.insert(aaEvent);
        eDao.insert(bEvent);
        eDao.insert(bbEvent);

        ArrayList<Event> foundEvents = eDao.findTypeByPerson(aEvent.getPersonID(), aEvent.getEventType());

        assertNotNull(foundEvents);
        assertEquals(2, foundEvents.size());
        assertEquals(aEvent, foundEvents.get(0));
        assertEquals(aaEvent, foundEvents.get(1));
    }

    @Test
    public void findTypeByPersonFail() throws DataAccessException {
        eDao.insert(aEvent);
        eDao.insert(aaEvent);
        eDao.insert(bEvent);
        eDao.insert(bbEvent);

        ArrayList<Event> foundEvents = eDao.findTypeByPerson(aEvent.getPersonID(), bEvent.getEventType());

        assertNull(foundEvents);
    }

    @Test
    public void findAllForUserPass() throws DataAccessException {
        eDao.insert(aEvent);
        eDao.insert(aaEvent);
        eDao.insert(bEvent);
        eDao.insert(bbEvent);

        ArrayList<Event> foundEvents = eDao.findAllForUser(aEvent.getAssociatedUsername());

        assertNotNull(foundEvents);
        assertEquals(2, foundEvents.size());
        assertEquals(aEvent, foundEvents.get(0));
        assertEquals(aaEvent, foundEvents.get(1));
    }

    @Test
    public void findAllForUserFail() throws DataAccessException {
        eDao.insert(aEvent);
        eDao.insert(aaEvent);
        eDao.insert(bEvent);
        eDao.insert(bbEvent);

        ArrayList<Event> foundEvents = eDao.findAllForUser("Jerrinaldo");

        assertNull(foundEvents);
    }

    @Test
    public void clearPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(anotherEvent);

        eDao.clear();

        Event compareTest = eDao.find(bestEvent.getEventID());
        ArrayList<Event> compareAllTest = eDao.findAll();

        assertNull(compareTest);
        assertNull(compareAllTest);
    }

    @Test
    public void clearByIdPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(anotherEvent);

        eDao.clear("Gale");

        assertNull(eDao.find(bestEvent.getEventID()));
        assertNotNull(eDao.find(anotherEvent.getEventID()));
    }

    @Test
    public void clearPersonEventByTypePass() throws DataAccessException {
        eDao.insert(aEvent);
        eDao.insert(aaEvent);
        eDao.insert(bEvent);
        eDao.insert(bbEvent);

        assertNotNull(eDao.find(aEvent.getEventID()));
        assertNotNull(eDao.find(aaEvent.getEventID()));
        assertNotNull(eDao.find(bbEvent.getEventID()));
        assertNotNull(eDao.find(bEvent.getEventID()));

        eDao.clearPersonEventByType("aEventPersonID", "aEventType");

        assertNull(eDao.find(aEvent.getEventID()));
        assertNull(eDao.find(aaEvent.getEventID()));
        assertNotNull(eDao.find(bEvent.getEventID()));
        assertNotNull(eDao.find(bbEvent.getEventID()));
    }

}
