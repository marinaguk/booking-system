package bookingApp.service;

import bookingApp.dto.*;
import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import bookingApp.mapper.BookingMapper;
import bookingApp.mapper.PropertyMapper;
import bookingApp.model.Role;
import bookingApp.model.SearchBookingResult;
import bookingApp.model.SearchPropertyResult;
import bookingApp.repository.BookingRepository;
import bookingApp.repository.PropertyRepository;
import bookingApp.repository.UserRepository;
import bookingApp.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static bookingApp.util.GsonUtil.errorToJson;

public class PropertyService {

    private static final Logger logger =
            LoggerFactory.getLogger(PropertyService.class);

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public PropertyService(PropertyRepository propertyRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public int addProperty(UserEntity owner, AddPropertyRequest addPropertyRequest) {
        PropertyEntity propertyEntity = addPropertyRequest.propertyRequestToEntity(owner);

        if (propertyRepository.findByName(propertyEntity.getPropertyName()) != null) {
            throw new BadRequestException("Property already exist");
        }

        propertyRepository.save(propertyEntity);

        logger.info("Property is added. User id: {}", owner.getId());

        return propertyEntity.getPropertyId();
    }

    public SearchPropertyResponse search(SearchPropertyRequest request, int page, int size, String sortBy, String sortDirection) {
        int offset = (page - 1) * size;
        SearchPropertyResult searchProperties = propertyRepository.search(request, offset, size, sortBy, sortDirection);

        long totalItems = searchProperties.getTotalItems();
        List<PropertyResponse> responseList = PropertyMapper.convertPropertyEntityToResponseList(searchProperties.getItems());
        int totalPages = (int)Math.ceil((double) totalItems /size);

        SearchPropertyResponse response = new SearchPropertyResponse();
        response.setItems(responseList);
        response.setPage(page);
        response.setSize(size);
        response.setTotalItems(totalItems);
        response.setTotalPages(totalPages);;

        return response;
    }


    public void delete(int userId, int propertyId) {
        PropertyEntity propertyEntity = propertyRepository.findById(propertyId);
        UserEntity userEntity = userRepository.findById(userId);

        if (propertyEntity == null) {
            throw new NotFoundException("Property not found");
        }

        if (propertyEntity.getOwner().getId() != userEntity.getId() && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("This property cannot be deleted");
        }

        propertyRepository.deleteById(propertyId);

        logger.info("Property deleted. User id: {}, Property id: {}", userId, propertyId);
    }

    public PropertyAvailabilityResponse getAvailability(int propertyId) {
        PropertyEntity propertyEntity = propertyRepository.findById(propertyId);
        if (propertyEntity == null) {
            throw new NotFoundException("Property not found");
        }

        List<BookingEntity> bookingEntityList = bookingRepository.findByPropertyId(propertyId);

        PropertyAvailabilityResponse response = new PropertyAvailabilityResponse();

        response.setId(propertyId);

        response.setBusyDatesResponseList(
                BookingMapper.convertBookingEntityToBusyDatesResponse(bookingEntityList)
        );

        return response;
    }

    public AllBookingResponse getAllBooking(int userId, int propertyId, int page, int size) {
        int offset = (page - 1) * size;

        PropertyEntity propertyEntity = propertyRepository.findById(propertyId);
        UserEntity userEntity = userRepository.findById(userId);

        if (propertyEntity == null) {
            throw new NotFoundException("Property not found");
        }

        if (propertyEntity.getOwner().getId() != userEntity.getId() && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("Inaccessible");
        }

        SearchBookingResult result = bookingRepository.getAllBookingByPropertyId(propertyId,offset,size);

        long totalItems = result.getTotalItems();
        List<AllInfoBookingResponse> responseList
                = PropertyMapper.convertPropertyEntityToBookingAllInfoList(result.getItems());
        int totalPages = (int)Math.ceil((double) totalItems /size);

        AllBookingResponse response = new AllBookingResponse();
        response.setBookingResponseList(responseList);
        response.setTotalBookings(totalItems);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(totalPages);

        return response;
    }

    public PropertyResponse getById(int id) {
        PropertyEntity propertyEntity = propertyRepository.findById(id);

        if (propertyEntity == null) {
            throw new NotFoundException("Property not found");
        }

        return PropertyMapper.convertPropertyEntityToResponse(propertyEntity);
    }

}
