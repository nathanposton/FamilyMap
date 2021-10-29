package Result.Responses;

import Result.Response;

/**
 * Clear API Results
 */
public class ClearResult extends Response {

    //success
    public ClearResult() {
        this.message = "Clear succeeded.";
        this.success = true;
    }

    public ClearResult(Exception ex) {
        super(ex);
    }

}
