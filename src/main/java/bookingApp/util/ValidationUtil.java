package bookingApp.util;

import bookingApp.exception.BadRequestException;
import bookingApp.exception.UnauthorizedException;

import java.time.LocalDate;

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


}
