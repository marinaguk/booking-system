package bookingApp.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class SearchPropertyRequest {

    private String city;

    @PositiveOrZero(message = "Min price cannot be negative")
    private Double minPrice;

    @PositiveOrZero(message = "Max price cannot be negative")
    private Double maxPrice;

    private LocalDate startDate;
    private LocalDate endDate;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
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
