package bookingApp.dto;

import java.util.List;

public class PropertyAvailabilityResponse {

    private int id;
    private List<BusyDateResponse> busyDatesResponseList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<BusyDateResponse> getBusyDatesResponseList() {
        return busyDatesResponseList;
    }

    public void setBusyDatesResponseList(List<BusyDateResponse> busyDatesResponseList) {
        this.busyDatesResponseList = busyDatesResponseList;
    }
}
