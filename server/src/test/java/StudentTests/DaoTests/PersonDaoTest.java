package StudentTests.DaoTests;

import ServerClasses.DataAccess.DataAccessException;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDaoTest {
    private Database db;
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
    private Person motherFatherCopy;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {

        db = new Database();

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
        motherFatherCopy = new Person("motherFatherID", "childUsername", "motherFatherFirstName",
                "motherFatherLastName", "m", null, null, "motherMotherID");

        Connection conn = db.getConnection();

        db.clearTables();

        pDao = new PersonDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(bestPerson);

        Person compareTest = pDao.findOne(bestPerson.getPersonID());

        assertNotNull(compareTest);

        assertEquals(bestPerson, compareTest);

        pDao.insert(relatedPerson);

        compareTest = pDao.findOne(relatedPerson.getPersonID());

        assertNotNull(compareTest);
        assertEquals(relatedPerson, compareTest);

    }

    @Test
    public void insertFail() throws DataAccessException {

        pDao.insert(bestPerson);

        assertThrows(DataAccessException.class, ()-> pDao.insert(bestPerson));
    }

    @Test
    public void findOnePass() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(relatedPerson);

        //Test findOne
        Person compareTest = pDao.findOne(bestPerson.getPersonID());
        Person compareTest1 = pDao.findOne(relatedPerson.getPersonID());

        assertNotNull(compareTest);
        assertNotNull(compareTest1);

        assertEquals(bestPerson, compareTest);
        assertEquals(relatedPerson, compareTest1);
    }

    @Test
    public void findOneFail() throws DataAccessException {
        assertNull(pDao.findOne(relatedPerson.getPersonID()));
    }

    @Test
    public void findAllPass() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(relatedPerson);

        ArrayList<Person> compareAllTest = pDao.findAll();

        assertNotNull(compareAllTest);

        assertEquals(bestPerson, compareAllTest.get(0));
        assertEquals(relatedPerson, compareAllTest.get(1));
    }

    @Test
    public void findAllFail() throws DataAccessException {
        assertNull(pDao.findAll());
    }

    @Test
    public void findAllForUserPass() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(relatedPerson);

        ArrayList<Person> compareAllTest = pDao.findAllForUser(bestPerson.getAssociatedUsername());

        assertNotNull(compareAllTest);

        assertEquals(1, compareAllTest.size());
        assertEquals(bestPerson, compareAllTest.get(0));
    }

    @Test
    public void findAllForUserFail() throws DataAccessException {
        assertNull(pDao.findAllForUser(bestPerson.getAssociatedUsername()));
    }


    @Test
    public void clearPass() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(relatedPerson);

        Person compareTest = pDao.findOne(bestPerson.getPersonID());

        assertNotNull(compareTest);

        pDao.clear();

        compareTest = pDao.findOne(bestPerson.getPersonID());
        ArrayList<Person> compareAllTest = pDao.findAll();

        assertNull(compareTest);
        assertNull(compareAllTest);
    }

    @Test
    public void clearByUsernamePass() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(relatedPerson);

        pDao.insert(child);
        pDao.insert(spouse);
        pDao.insert(mother);
        pDao.insert(father);
        pDao.insert(motherFather);
        pDao.insert(motherMother);
        pDao.insert(fatherMother);
        pDao.insert(fatherFather);

        Person compareTest = pDao.findOne(bestPerson.getPersonID());
        assertNotNull(pDao.findOne(child.getPersonID()));
        assertNotNull(pDao.findOne(mother.getPersonID()));
        assertNotNull(pDao.findOne(fatherFather.getPersonID()));

        assertNotNull(compareTest);

        pDao.clearByUsername("childUsername");

        compareTest = pDao.findOne(bestPerson.getPersonID());
        ArrayList<Person> compareAllTest = pDao.findAll();

        assertNotNull(compareTest);
        assertNotNull(compareAllTest);
        assertNull(pDao.findOne(child.getPersonID()));
        assertNull(pDao.findOne(mother.getPersonID()));
        assertNull(pDao.findOne(father.getPersonID()));
        assertNull(pDao.findOne(motherFather.getPersonID()));
        assertNull(pDao.findOne(fatherFather.getPersonID()));
        assertNotNull(pDao.findOne(spouse.getPersonID()));
    }

    @Test
    public void deleteOne() throws DataAccessException {

        pDao.insert(child);
        pDao.insert(spouse);
        pDao.insert(mother);
        pDao.insert(father);
        pDao.insert(motherFather);
        pDao.insert(motherMother);
        pDao.insert(fatherMother);
        pDao.insert(fatherFather);

        pDao.clear(motherFather.getPersonID());

        Person compareTest = pDao.findOne(motherFather.getPersonID());
        Person compareTestSpouse = pDao.findOne(motherMother.getPersonID());
        Person compareTestChild = pDao.findOne(mother.getPersonID());

        assertNull(compareTest);
        assertNotNull(compareTestSpouse);

        pDao.clear(father.getPersonID());

        compareTest = pDao.findOne(father.getPersonID());
        Person compareTestAncestor = pDao.findOne(fatherMother.getPersonID());
        compareTestSpouse = pDao.findOne(mother.getPersonID());

        assertNull(compareTest);
        assertNotNull(compareTestSpouse);
        assertNotNull(compareTestAncestor);
    }

    @Test
    public void PersonModelEqualsPass() {
        assertFalse(motherFather.equals(mother));
        assertTrue(motherFather.equals(motherFatherCopy));

    }
}
