package ServerClasses.Service.Services;

import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.Database;
import ServerClasses.DataAccess.PersonDao;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.SinglePersonServiceRequest;
import Result.Responses.LoginResult;
import Result.Responses.SinglePersonServiceResult;

public class SinglePersonService {
    /**
     * executes person/[personID] API
     * @param r object contains request body information
     * @return SinglePersonServiceResult object containing result body information
     */
    public static SinglePersonServiceResult singlePerson(SinglePersonServiceRequest r) {
        Database db = new Database();
        if (r == null) {
            return new SinglePersonServiceResult(new Exception());
        }

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.verify(r.getAuthtoken());

            PersonDao personDao = new PersonDao(db.getConnection());

            Person person = personDao.findOne(r.getPersonID());

            db.closeConnection(true);

            if (person.getAssociatedUsername().equals(authToken.getUsername())) {
                return new SinglePersonServiceResult(person.getAssociatedUsername(), person.getPersonID(),
                        person.getFirstName(), person.getLastName(), person.getGender(),
                        person.getFatherID(), person.getMotherID(), person.getSpouseID());
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
            return new SinglePersonServiceResult(ex);
        }
    }
}
