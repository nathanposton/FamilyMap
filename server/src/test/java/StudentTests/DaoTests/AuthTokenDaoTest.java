package StudentTests.DaoTests;

import ServerClasses.DataAccess.DataAccessException;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.AuthTokenDao;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthTokenDaoTest {
    private Database db;
    private AuthToken bestAuthToken;
    private AuthToken relatedAuthToken;
    private AuthToken aToken;
    private AuthToken bToken;
    private AuthToken cToken;
    private AuthToken dToken;
    private AuthTokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException
    {

        db = new Database();

        bestAuthToken = new AuthToken("authtoken123", "username234");
        relatedAuthToken = new AuthToken("ANOTHERauthtoken123", "ANOTHERusername234");
        cToken = new AuthToken("ERauthtoken123", "alphabet");
        aToken = new AuthToken("oken123", "alphabet");
        bToken = new AuthToken("n123", "alphabet");
        dToken = new AuthToken("OTHERauthtoken123", "ANOTHERusername234");

        Connection conn = db.getConnection();

        db.clearTables();

        aDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(bestAuthToken);

        AuthToken compareTest = aDao.find(bestAuthToken.getUsername());

        assertNotNull(compareTest);

        assertEquals(bestAuthToken, compareTest);

        aDao.insert(relatedAuthToken);

        compareTest = aDao.find(relatedAuthToken.getUsername());

        assertNotNull(compareTest);
        assertEquals(relatedAuthToken, compareTest);

    }

    @Test
    public void insertFail() throws DataAccessException {

        aDao.insert(bestAuthToken);

        assertThrows(DataAccessException.class, ()-> aDao.insert(bestAuthToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        aDao.insert(relatedAuthToken);

        //Test find
        AuthToken compareTest = aDao.find(bestAuthToken.getUsername());
        AuthToken compareTest1 = aDao.find(relatedAuthToken.getUsername());

        assertNotNull(compareTest);
        assertNotNull(compareTest1);

        assertEquals(bestAuthToken, compareTest);
        assertEquals(relatedAuthToken, compareTest1);
    }

    @Test
    public void findFail() throws DataAccessException {

        assertNull(aDao.find(relatedAuthToken.getUsername()));
    }

    @Test
    public void verifyPass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        aDao.insert(relatedAuthToken);

        //Test find
        AuthToken compareTest = aDao.verify(bestAuthToken.getAuthtoken());
        AuthToken compareTest1 = aDao.verify(relatedAuthToken.getAuthtoken());

        assertNotNull(compareTest);
        assertNotNull(compareTest1);

        assertEquals(bestAuthToken, compareTest);
        assertEquals(relatedAuthToken, compareTest1);
    }

    @Test
    public void verifyFail() throws DataAccessException {

        assertNull(aDao.find(relatedAuthToken.getAuthtoken()));
    }

    @Test
    public void clearPass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        aDao.insert(relatedAuthToken);
        aDao.insert(aToken);
        aDao.insert(bToken);
        aDao.insert(cToken);
        aDao.insert(dToken);

        aDao.clear();

        assertNull(aDao.find(aToken.getUsername()));
        assertNull(aDao.find(bToken.getUsername()));
        assertNull(aDao.find(cToken.getUsername()));
        assertNull(aDao.find(dToken.getUsername()));

        aDao.clear();

        AuthToken compareTest = aDao.find(bestAuthToken.getUsername());

        assertNull(compareTest);
    }

    @Test
    public void clearUsernamePass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        aDao.insert(relatedAuthToken);
        aDao.insert(aToken);
        aDao.insert(bToken);
        aDao.insert(cToken);
        aDao.insert(dToken);

        aDao.clear(aToken.getUsername());

        assertNull(aDao.find(aToken.getUsername()));
        assertNull(aDao.find(bToken.getUsername()));
        assertNull(aDao.find(cToken.getUsername()));
        assertNotNull(aDao.find(dToken.getUsername()));

        aDao.clear();

        AuthToken compareTest = aDao.find(bestAuthToken.getUsername());

        assertNull(compareTest);
    }
}
