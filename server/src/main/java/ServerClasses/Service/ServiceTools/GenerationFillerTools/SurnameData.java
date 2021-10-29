package ServerClasses.Service.ServiceTools.GenerationFillerTools;

import java.util.List;

public class SurnameData {
    private List<String> data;

    public SurnameData() {}

    public SurnameData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
