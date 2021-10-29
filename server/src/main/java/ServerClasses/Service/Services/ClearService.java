package ServerClasses.Service.Services;

import ServerClasses.DataAccess.Database;
import Result.Responses.ClearResult;

public class ClearService {

    /**
     * executes the clear API
     * @return ClearResult object
     */
    public static ClearResult clear() {
        Database db = new Database();

        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult();
        } catch (Exception ex) {
            return new ClearResult(ex);
        }
    }
}
