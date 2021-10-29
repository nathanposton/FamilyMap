package ServerClasses.Service.ServiceTools.GenerationFillerTools;

import java.util.List;

public class LocationData {
    private List<Location> data;

    LocationData(){}

    public LocationData(List<Location> data) {
        this.data = data;
    }

    public List<Location> getData() {
        return data;
    }

    public void setData(List<Location> data) {
        this.data = data;
    }
}
