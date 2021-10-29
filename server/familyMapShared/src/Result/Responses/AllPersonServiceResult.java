package Result.Responses;

import Model.Person;
import Result.Response;

import java.util.List;

/**
 * All Persons API Results
 */
public class AllPersonServiceResult extends Response {

    private List<Person> data;

    public AllPersonServiceResult(List<Person> data) {
        this.data = data;
        this.success = true;
    }

    public AllPersonServiceResult(Exception ex) {
        super(ex);
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
