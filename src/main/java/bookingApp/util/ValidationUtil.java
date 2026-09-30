package bookingApp.util;

import bookingApp.exception.BadRequestException;
import bookingApp.exception.UnauthorizedException;

import java.time.LocalDate;
import java.util.List;

public class ValidationUtil {

    public static void requireValidPriceRange(Double minPrice, Double maxPrice) {
        if (maxPrice != null && minPrice != null && maxPrice < minPrice) {
            throw new BadRequestException("Max price cannot be less than min price");
        }
    }

    public static void requireStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            throw new BadRequestException("Both dates are required");
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

    public static void requireValidSortDirection(String sortDirection) {

        List<String> allowedDirections = List.of("asc", "desc");

        if (!allowedDirections.contains(sortDirection)) {
            throw new BadRequestException("Invalid sort direction");
        }
    }

    public static int requireValidSessionId(String sessionId) {
        Integer userId = SessionManager.getUserId(sessionId);
        if (userId == null) {throw new UnauthorizedException("Unauthorized");
        }
        return userId;
    }


}
