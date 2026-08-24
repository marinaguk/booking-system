package bookingApp.mapper;

import bookingApp.dto.BookingResponse;
import bookingApp.dto.BusyDateResponse;
import bookingApp.entity.BookingEntity;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static List<BusyDateResponse> convertBookingEntityToBusyDatesResponse(List<BookingEntity> bookingEntityList) {
        List<BusyDateResponse> busyDatesResponseList = new ArrayList<>();

        for (BookingEntity bookingEntity : bookingEntityList) {
            BusyDateResponse busyDatesResponse = new BusyDateResponse();
            busyDatesResponse.setStartDate(bookingEntity.getStartDate());
            busyDatesResponse.setEndDate(bookingEntity.getEndDate());
            busyDatesResponseList.add(busyDatesResponse);
        }
        return busyDatesResponseList;
    }

    public static List<BookingResponse> convertEntityToResponseList(List<BookingEntity> entityList) {

        List<BookingResponse> responseList = new ArrayList<>();

        for (BookingEntity bookingEntity : entityList) {
            BookingResponse response = new BookingResponse();
            response.setId(bookingEntity.getId());
            response.setPropertyName(bookingEntity.getPropertyEntity().getPropertyName());
            response.setStartDate(bookingEntity.getStartDate());
            response.setEndDate(bookingEntity.getEndDate());
            responseList.add(response);
        }

        return responseList;
    }

    public static BookingResponse convertBookingEntityToResponse(BookingEntity entity) {
        return new BookingResponse(entity.getId(),
                entity.getPropertyEntity().getPropertyName(),
                entity.getStartDate(),
                entity.getEndDate());
    }

}
