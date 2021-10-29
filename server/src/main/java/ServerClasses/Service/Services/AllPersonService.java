package ServerClasses.Service.Services;

import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.PersonDao;
import Model.AuthToken;
import Request.AllPersonServiceRequest;
import Result.Responses.AllEventServiceResult;
import Result.Responses.AllPersonServiceResult;

public class AllPersonService {
    /**
     * executes person API
     * @param r object contains request body information
     * @return AllPersonServiceResult object containing result body information
     */
    public static AllPersonServiceResult allPerson(AllPersonServiceRequest r) {
        Database db = new Database();
        if (r == null) {
            return new AllPersonServiceResult(new Exception());
        }

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.verify(r.getAuthtoken());

            PersonDao personDao = new PersonDao(db.getConnection());

            String username = authToken.getUsername();
            AllPersonServiceResult result = new AllPersonServiceResult(personDao.findAllForUser(username));

            db.closeConnection(true);

            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new AllPersonServiceResult(ex);
        }
    }
}
