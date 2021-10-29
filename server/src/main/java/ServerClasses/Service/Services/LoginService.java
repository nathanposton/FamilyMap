package ServerClasses.Service.Services;

import ServerClasses.DataAccess.*;
import ServerClasses.DataAccess.AuthTokenDao;
import ServerClasses.DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.Responses.LoginResult;
import ServerClasses.Service.ServiceTools.AuthTokenGenerator;

public class LoginService {

    /**
     * executes load API
     *
     * @param r object contains request body information
     * @return LoadResult object containing result body information
     */
    public static LoginResult login(LoginRequest r) {
        Database db = new Database();
        if (r == null) {
            return new LoginResult(new Exception());
        }

        try {
            db.openConnection();

            UserDao userDao = new UserDao(db.getConnection());
            User user = userDao.find(r.getUsername());

            if ( !(user.getPassword().equals(r.getPassword())) ) {
                throw new Exception();
            }

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = new AuthToken(AuthTokenGenerator.generate(), r.getUsername());
            authTokenDao.clear(r.getUsername());
            authTokenDao.insert(authToken);

            db.closeConnection(true);

            return new LoginResult(authToken.getAuthtoken(), authToken.getUsername(), user.getPersonID());

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error closing connection");
            }
            return new LoginResult(ex);
        }
    }
}
