package bookingApp.dto;

import java.time.LocalDate;

public class BookingResponse {

    private int id;
    private String propertyName;
    private LocalDate startDate;
    private LocalDate endDate;

    public BookingResponse(int id, String propertyName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.propertyName = propertyName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public BookingResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
