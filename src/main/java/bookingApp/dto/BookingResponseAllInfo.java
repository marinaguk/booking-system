package bookingApp.dto;

import java.time.LocalDate;

public class BookingResponseAllInfo {

    private int bookingId;
    private int propertyId;
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;

    public BookingResponseAllInfo(int bookingId, int propertyId, int userId, LocalDate startDate, LocalDate endDate) {
        this.bookingId = bookingId;
        this.propertyId = propertyId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
