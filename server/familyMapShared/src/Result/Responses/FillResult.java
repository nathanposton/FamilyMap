package Result.Responses;

import Result.Response;

/**
 * Fill API Results
 */
public class FillResult extends Response {

    //success
    public FillResult(int personsAdded, int eventsAdded) {
        this.message = "Successfully added " + personsAdded + " persons and " +
                eventsAdded + " events to the database.";
        this.success = true;
    }

    public FillResult(Exception ex) {
        super(ex);
    }

}
