package bookingApp.service;

import bookingApp.mapper.BookingMapper;
import bookingApp.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bookingApp.dto.CreateBookingRequest;
import bookingApp.dto.BookingResponse;
import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import bookingApp.exception.AccessDeniedException;
import bookingApp.exception.BadRequestException;
import bookingApp.exception.NotFoundException;
import bookingApp.repository.BookingRepository;
import bookingApp.repository.PropertyRepository;
import bookingApp.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

public class BookingService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingService.class);

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    public BookingService(PropertyRepository propertyRepository,
                          UserRepository userRepository,
                          BookingRepository bookingRepository,
                          UserService userService) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.userService = userService;
    }

    public int createBooking(int userId, CreateBookingRequest request) {
        UserEntity userEntity = userRepository.findById(userId);
        PropertyEntity propertyEntity = propertyRepository.findById(request.getPropertyId());

        if (propertyEntity == null) {
            throw new NotFoundException("Property not found");
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Incorrect date");
        }

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setUserEntity(userEntity);
        bookingEntity.setPropertyEntity(propertyEntity);
        bookingEntity.setStartDate(startDate);
        bookingEntity.setEndDate(endDate);

        if (!isBookingDateFree(bookingEntity.getPropertyEntity().getPropertyId(), startDate, endDate)) {
            throw new BadRequestException("Date is busy");
        }

        bookingRepository.save(bookingEntity);
        logger.info("Booking is created. User id: {}", userId);
        return  bookingEntity.getId();
    }

    public List<BookingResponse> searchMyBooking(int userId, String sortBy, String sortDirection) {

        List<BookingEntity> bookingEntityList = bookingRepository.searchByUserId(userId, sortBy, sortDirection);

        if (bookingEntityList.isEmpty()) {
            return null;
        }

        List<BookingResponse> responseList = BookingMapper.convertEntityToResponseList(bookingEntityList);

        return responseList;
    }

    public boolean isBookingDateFree(int propertyId, LocalDate startDate, LocalDate endDate) {

        List<BookingEntity> existBookings = bookingRepository.findByPropertyId(propertyId);

        for (BookingEntity bookingEntity : existBookings) {
            LocalDate startDateBooking = bookingEntity.getStartDate();
            LocalDate endDateBooking = bookingEntity.getEndDate();
            if (!(endDate.isBefore(startDateBooking) || startDate.isAfter(endDateBooking))) {
                return false;
            }
        }
        return true;

    }

    public void deleteBooking(int userId, int bookingId) {

        BookingEntity bookingEntity = bookingRepository.findById(bookingId);
        UserEntity userEntity = userService.getUserEntityById(userId);

        if (bookingEntity == null) {
            throw new NotFoundException("Booking not found");
        }

        if (bookingEntity.getUserEntity().getId() != userId && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("This booking cannot be deleted");
        }

        bookingRepository.deleteById(bookingId);

        logger.info("Booking is deleted. User id: {}", userId);
    }

    public BookingResponse getBookingById(int bookingId, int userId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId);
        UserEntity userEntity = userService.getUserEntityById(userId);

        if (bookingEntity == null) {
            throw new NotFoundException("Booking not found");
        }

        if (bookingEntity.getUserEntity().getId() != userId && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("No access");
        }

        BookingResponse response = BookingMapper.convertBookingEntityToResponse(bookingEntity);
        return response;
    }


}
