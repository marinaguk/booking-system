package bookingApp.util;

import bookingApp.exception.BadRequestException;

import java.time.LocalDate;
import java.util.List;

public class ValidationUtil {

    public static void requireNotEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(message);
        }
    }

    public static void requireNotNull(Object object, String message) {
        if (object == null) {
            throw new BadRequestException(message);
        }
    }

    public static void requirePositive(int value, String message) {
        if (value <= 0) {
            throw new BadRequestException(message);
        }
    }

    public static void requireValidPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new BadRequestException("Min price cannot be negative");
        }

        if (maxPrice != null && maxPrice < 0) {
            throw new BadRequestException("Max price cannot be negative");
        }

        if (maxPrice != null && minPrice != null && maxPrice < minPrice) {
            throw new BadRequestException("Max price cannot be less than min price");
        }
    }

    public static void requireStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            throw new BadRequestException("Both dates are required");
        }
    }

    public static void requireValidPageSize(int page, int size) {
        if (page <= 0 || size <= 0 || size > 100) {
            throw new BadRequestException("Page and size must be positive. Max size is 100");
        }
    }

    public static void requireValidPropertySort(String sortBy, String sortDirection) {

        List<String> allowedFields = List.of("price", "name");
        List<String> allowedDirections = List.of("asc", "desc");

        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException("Invalid sort field");
        }

        if (!allowedDirections.contains(sortDirection)) {
            throw new BadRequestException("Invalid sort direction");
        }
    }

    public static void requireValidBookingSort(String sortBy, String sortDirection) {

        List<String> allowedFields = List.of("date");
        List<String> allowedDirections = List.of("asc", "desc");

        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException("Invalid sort field");
        }

        if (!allowedDirections.contains(sortDirection)) {
            throw new BadRequestException("Invalid sort direction");
        }
    }

}
