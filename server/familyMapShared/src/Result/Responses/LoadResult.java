package Result.Responses;

import Result.Response;

/**
 * Load API Results
 */
public class LoadResult extends Response {

    //success
    public LoadResult(int usersAdded, int personsAdded, int eventsAdded) {
        this.message = "Successfully added " + usersAdded + " users, " +
                       personsAdded + " persons, and " +
                       eventsAdded + " events to the database.";
        this.success = true;
    }

    public LoadResult(Exception ex) {
        super(ex);
    }
}
