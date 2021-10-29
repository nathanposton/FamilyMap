package ServerClasses.Service.Services;

import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Request.SingleEventServiceRequest;
import Result.Responses.SingleEventServiceResult;

public class SingleEventService {
    /**
     * executes event/[eventID] API
     * @param r object contains request body information
     * @return SingleEventServiceResult object containing result body information
     */
    public static SingleEventServiceResult singleEvent(SingleEventServiceRequest r) {
        Database db = new Database();
        if (r == null) {
            return new SingleEventServiceResult(new Exception());
        }

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.verify(r.getAuthtoken());

            EventDao eventDao = new EventDao(db.getConnection());

            Event event = eventDao.find(r.getEventID());

            db.closeConnection(true);

            if (authToken != null && event.getAssociatedUsername().equals(authToken.getUsername())) {
                return new SingleEventServiceResult(event.getEventID(), event.getAssociatedUsername(),
                        event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(),
                        event.getCity(), event.getEventType(), event.getYear());
            }
            else {
                throw new Exception();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new SingleEventServiceResult(ex);
        }
    }
}
