package bookingApp.dto;

import java.util.List;

public class SearchPropertyResponse {

    private List<PropertyResponse> items;

    private int page;

    private int size;

    private int totalPages;

    private long totalItems;

    public List<PropertyResponse> getItems() {
        return items;
    }

    public void setItems(List<PropertyResponse> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
