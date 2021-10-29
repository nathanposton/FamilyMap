package ServerClasses.Service.Services;

import ServerClasses.DataAccess.*;
import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.EventDao;
import ServerClasses.DataAccess.PersonDao;
import ServerClasses.DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Request.RegisterRequest;
import Result.Responses.RegisterResult;
import ServerClasses.Service.ServiceTools.AuthTokenGenerator;
import ServerClasses.Service.ServiceTools.GenerationFiller;

public class RegisterService {

    /**
     * executes the register API
     * @param r contains request body information
     * @return register results
     */
    public static RegisterResult register(RegisterRequest r) {
        Database db = new Database();
        if (r == null) {
            return new RegisterResult(new Exception());
        }

        try {
            db.openConnection();

            EventDao eventDao = new EventDao(db.getConnection());
            PersonDao personDao = new PersonDao(db.getConnection());
            UserDao userDao = new UserDao(db.getConnection());

            User user = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                                 r.getLastName(), r.getGender(), AuthTokenGenerator.generate());
            userDao.insert(user);

            GenerationFiller generationFiller = new GenerationFiller(personDao, eventDao, user);
            generationFiller.cleanUser();
            generationFiller.doFill(4);

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = new AuthToken(AuthTokenGenerator.generate(), r.getUsername());
            authTokenDao.clear(r.getUsername());
            authTokenDao.insert(authToken);

            db.closeConnection(true);

            return new RegisterResult(authToken.getAuthtoken(), r.getUsername(), user.getPersonID());

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new RegisterResult(ex);
        }

    }
}
