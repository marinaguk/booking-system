package bookingApp.model;

import bookingApp.entity.PropertyEntity;

import java.util.List;

public class SearchPropertyResult {

    private List<PropertyEntity> items;
    private long totalItems;

    public List<PropertyEntity> getItems() {
        return items;
    }

    public void setItems(List<PropertyEntity> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
