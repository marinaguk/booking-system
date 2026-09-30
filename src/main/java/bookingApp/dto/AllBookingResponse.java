package bookingApp.dto;

import java.util.List;

public class AllBookingResponse {

    private long totalBookings;

    private int page;

    private int size;

    private int totalPages;

    private List<BookingResponseAllInfo> bookingResponseList;

    public AllBookingResponse(long totalBookings, int page, int size, int totalPages, List<BookingResponseAllInfo> bookingResponseList) {
        this.totalBookings = totalBookings;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.bookingResponseList = bookingResponseList;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public List<BookingResponseAllInfo> getBookingResponseList() {
        return bookingResponseList;
    }

    public void setBookingResponseList(List<BookingResponseAllInfo> bookingResponseList) {
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
