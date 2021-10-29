package ServerClasses.Service.Services;

import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.EventDao;
import ServerClasses.DataAccess.PersonDao;
import ServerClasses.DataAccess.UserDao;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.Responses.LoadResult;
import Result.Responses.RegisterResult;

public class LoadService {
    /**
     * executes load API
     *
     * @param r object contains request body information
     * @return LoadResult object containing result body information
     */
    public static LoadResult load(LoadRequest r) {
        Database db = new Database();
        if (r == null) {
            return new LoadResult(new Exception());
        }

        try {
            db.openConnection();
            db.clearTables();

            UserDao userData = new UserDao(db.getConnection());
            PersonDao personData = new PersonDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());

            for (User user : r.getUsers()) {
                userData.insert(user);
            }
            for (Person person : r.getPersons()) {
                personData.insert(person);
            }
            for (Event event : r.getEvents()) {
                eventDao.insert(event);
            }

            db.closeConnection(true);
            return new LoadResult(r.getUsers().size(), r.getPersons().size(), r.getEvents().size());

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new LoadResult(ex);
        }
    }
}
