package bookingApp.service;

import bookingApp.dto.*;
import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import bookingApp.mapper.BookingMapper;
import bookingApp.mapper.PropertyMapper;
import bookingApp.model.PropertySortField;
import bookingApp.model.Role;
import bookingApp.model.SearchPropertyResult;
import bookingApp.model.SortDirection;
import bookingApp.repository.BookingRepository;
import bookingApp.repository.PropertyRepository;
import bookingApp.repository.UserRepository;
import bookingApp.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
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

    @Transactional
    public int addProperty(UserEntity owner, AddPropertyRequest addPropertyRequest) {
        PropertyEntity propertyEntity = addPropertyRequest.propertyRequestToEntity(owner);
        propertyEntity.setPropertyCity(addPropertyRequest.getCity().toLowerCase());

        propertyRepository.save(propertyEntity);

        logger.info("Property is added. User id: {}", owner.getId());

        return propertyEntity.getPropertyId();
    }

    @Transactional(readOnly = true)
    public SearchPropertyResponse search(SearchPropertyRequest request, int page, int size, PropertySortField sortBy, SortDirection sortDirection) {
        int offset = (page - 1) * size;

        if (request.getCity() != null) {
            request.setCity(request.getCity().toLowerCase());
        }

        SearchPropertyResult searchProperties = propertyRepository.search(request, offset, size, sortBy, sortDirection);

        long totalItems = searchProperties.getTotalItems();
        List<PropertyResponse> responseList = PropertyMapper.convertPropertyEntityToResponseList(searchProperties.getItems());
        int totalPages = (int)Math.ceil((double) totalItems /size);

        SearchPropertyResponse response = new SearchPropertyResponse();
        response.setItems(responseList);
        response.setPage(page);
        response.setSize(size);
        response.setTotalItems(totalItems);
        response.setTotalPages(totalPages);

        return response;
    }

    @Transactional
    public void delete(int userId, int propertyId) {
        PropertyEntity propertyEntity = getEntityById(propertyId);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("User not found"));

        if (propertyEntity.getOwner().getId() != userId && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("This property cannot be deleted");
        }

        propertyRepository.deleteById(propertyId);

        logger.info("Property deleted. User id: {}, Property id: {}", userId, propertyId);
    }

    @Transactional(readOnly = true)
    public PropertyAvailabilityResponse getAvailability(int propertyId) {
        List<BookingEntity> bookingEntityList = bookingRepository.findActiveByPropertyId(propertyId);

        PropertyAvailabilityResponse response = new PropertyAvailabilityResponse();

        response.setId(propertyId);

        response.setBusyDatesResponseList(
                BookingMapper.convertBookingEntityToBusyDatesResponse(bookingEntityList)
        );

        return response;
    }

    @Transactional(readOnly = true)
    public AllBookingResponse getAllBooking(int userId, int propertyId, int page, int size) {
        int offset = (page - 1) * size;

        PropertyEntity propertyEntity = getEntityById(propertyId);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UnauthorizedException("User not found"));

        if (propertyEntity.getOwner().getId() != userId && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("Inaccessible");
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("startDate"));


        Page<BookingResponseAllInfo> bookingResponsePage = bookingRepository.findByPropertyEntityPropertyId(propertyId, pageable)
                .map(BookingMapper::convertBookingEntityToResponseAllInfo);

        AllBookingResponse allBookingResponse = new AllBookingResponse(bookingResponsePage.getTotalElements(),
                page, size, bookingResponsePage.getTotalPages(),  bookingResponsePage.getContent());

        return allBookingResponse;
    }

    @Transactional(readOnly = true)
    public PropertyResponse getResponseById(int id) {

        PropertyEntity propertyEntity = propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("Property not found"));

        return PropertyMapper.convertPropertyEntityToResponse(propertyEntity);
    }

    @Transactional(readOnly = true)
    public PropertyEntity getEntityById(int id) {
        return propertyRepository.findById(id).orElseThrow(() -> new NotFoundException("Property not found"));
    }

}
