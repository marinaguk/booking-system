package bookingApp.model;

import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;

import java.util.List;

public class SearchBookingResult {

    private List<BookingEntity> items;
    private long totalItems;

    public List<BookingEntity> getItems() {
        return items;
    }

    public void setItems(List<BookingEntity> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
