package bookingApp.mapper;

import bookingApp.dto.*;
import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;

import java.util.ArrayList;
import java.util.List;

public class PropertyMapper {

    public static List<PropertyResponse> convertPropertyEntityToResponseList(List<PropertyEntity> propertyEntityList) {
        List<PropertyResponse> responseList = new ArrayList<>();

        for (PropertyEntity propertyEntity : propertyEntityList) {
            PropertyResponse response = new PropertyResponse();
            response.setCity(propertyEntity.getPropertyCity());
            response.setName(propertyEntity.getPropertyName());
            response.setPrice(propertyEntity.getPropertyPrice());
            responseList.add(response);
        }
        return responseList;
    }

    public static List<AllInfoBookingResponse> convertPropertyEntityToBookingAllInfoList(List<BookingEntity> bookingEntityList) {
        List<AllInfoBookingResponse> result = new ArrayList<>();
        for (BookingEntity entity : bookingEntityList) {
            AllInfoBookingResponse response = new AllInfoBookingResponse();
            response.setBookingId(entity.getId());
            response.setPropertyId(entity.getPropertyEntity().getPropertyId());
            response.setUserId(entity.getUserEntity().getId());
            response.setStartDate(entity.getStartDate());
            response.setEndDate(entity.getEndDate());
            result.add(response);
        }
        return result;
    }

    public static PropertyResponse convertPropertyEntityToResponse(PropertyEntity entity) {
        PropertyResponse response = new PropertyResponse();
        response.setName(entity.getPropertyName());
        response.setCity(entity.getPropertyCity());
        response.setPrice(entity.getPropertyPrice());
        return response;
    }

}
