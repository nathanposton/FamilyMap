package Result.Responses;

import Model.Event;
import Result.Response;

import java.util.List;

/**
 * All Events API Results
 */
public class AllEventServiceResult extends Response {

    private List<Event> data;

    public AllEventServiceResult(List<Event> data) {
        this.data = data;
        this.success = true;
    }

    public AllEventServiceResult(Exception ex) {
        super(ex);
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
