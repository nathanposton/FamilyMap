package StudentTests.DaoTests;

import ServerClasses.DataAccess.DataAccessException;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.UserDao;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDaoTest {
    private Database db;
    private User bestUser;
    private User relatedUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {

        db = new Database();

        bestUser = new User("user3name", "pas2wsword", "emailaddress",
                "firstname", "lastname", "f", "IDafdsfasd342");
        relatedUser = new User("ANOTHERuser3name", "ANOTHERpas2wsword", "ANOTHERemailaddress",
                "ANOTHERfirstname", "ANOTHERlastname", "m", "anotherIDafdsfasd342");

        Connection conn = db.getConnection();

        db.clearTables();

        uDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(bestUser);

        User compareTest = uDao.find(bestUser.getUsername());

        assertNotNull(compareTest);

        assertEquals(bestUser, compareTest);

        uDao.insert(relatedUser);

        compareTest = uDao.find(relatedUser.getUsername());

        assertNotNull(compareTest);
        assertEquals(relatedUser, compareTest);

    }

    @Test
    public void insertFail() throws DataAccessException {

        uDao.insert(bestUser);

        assertThrows(DataAccessException.class, ()-> uDao.insert(bestUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insert(bestUser);
        uDao.insert(relatedUser);

        //Test find
        User compareTest = uDao.find(bestUser.getUsername());
        User compareTest1 = uDao.find(relatedUser.getUsername());

        assertNotNull(compareTest);
        assertNotNull(compareTest1);

        assertEquals(bestUser, compareTest);
        assertEquals(relatedUser, compareTest1);
    }

    @Test
    public void findFail() throws DataAccessException {

        assertNull(uDao.find(relatedUser.getUsername()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        uDao.insert(bestUser);
        uDao.insert(relatedUser);

        uDao.clear();

        User compareTest = uDao.find(bestUser.getUsername());

        assertNull(compareTest);
    }
}
