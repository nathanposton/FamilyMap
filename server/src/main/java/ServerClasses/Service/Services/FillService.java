package ServerClasses.Service.Services;

import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.EventDao;
import ServerClasses.DataAccess.PersonDao;
import ServerClasses.DataAccess.UserDao;
import Model.User;
import Request.FillRequest;
import Result.Responses.FillResult;
import ServerClasses.Service.ServiceTools.GenerationFiller;

public class FillService {

    /**
     * executes fill API
     * @param r object contains request body information
     * @return FillResult object containing result body information
     */
    public static FillResult fill(FillRequest r) {
        Database db = new Database();
        if (r == null) {
            return new FillResult(new Exception());
        }

        try {
            db.openConnection();

            PersonDao personDoa = new PersonDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());
            UserDao userDao = new UserDao(db.getConnection());

            User user = userDao.find(r.getUsername());

            GenerationFiller phil = new GenerationFiller(personDoa, eventDao, user);

            phil.cleanUser();
            phil.doFill(r.getGenerations());

            db.closeConnection(true);

            return new FillResult(phil.getUsersAdded(), phil.getEventsAdded());

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new FillResult(ex);
        }
    }
}
