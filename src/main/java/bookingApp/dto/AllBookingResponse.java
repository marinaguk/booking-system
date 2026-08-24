package bookingApp.dto;

import java.util.List;

public class AllBookingResponse {

    private long totalBookings;

    private int page;

    private int size;

    private int totalPages;

    private List<AllInfoBookingResponse> bookingResponseList;

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public List<AllInfoBookingResponse> getBookingResponseList() {
        return bookingResponseList;
    }

    public void setBookingResponseList(List<AllInfoBookingResponse> bookingResponseList) {
        this.bookingResponseList = bookingResponseList;
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
}
