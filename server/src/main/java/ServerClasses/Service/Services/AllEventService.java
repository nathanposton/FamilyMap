package ServerClasses.Service.Services;

import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.EventDao;
import Model.AuthToken;
import Request.AllEventServiceRequest;
import Result.Responses.AllEventServiceResult;
import Result.Responses.AllPersonServiceResult;

public class AllEventService {
    /**
     * executes event API
     * @param r object contains request body information
     * @return AllEventServiceResult object containing result body information
     */
    public static AllEventServiceResult allEvent(AllEventServiceRequest r) {
        Database db = new Database();
        if (r == null) {
            return new AllEventServiceResult(new Exception());
        }

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.verify(r.getAuthtoken());

            EventDao eventDao = new EventDao(db.getConnection());

            String username = authToken.getUsername();
            AllEventServiceResult result = new AllEventServiceResult(eventDao.findAllForUser(username));

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
            return new AllEventServiceResult(ex);
        }
    }
}
