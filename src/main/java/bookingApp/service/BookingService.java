package bookingApp.service;

import bookingApp.exception.UnauthorizedException;
import bookingApp.mapper.BookingMapper;
import bookingApp.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bookingApp.dto.CreateBookingRequest;
import bookingApp.dto.BookingResponse;
import bookingApp.entity.*;
import bookingApp.exception.*;
import bookingApp.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class BookingService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final PropertyService propertyService;

    public BookingService(BookingRepository bookingRepository,
                          UserService userService, PropertyService propertyService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @Transactional
    public int createBooking(int userId, CreateBookingRequest request) {
        UserEntity userEntity = userService.getUserEntityById(userId).orElseThrow(() -> new UnauthorizedException("Session is invalid"));
        PropertyEntity propertyEntity = propertyService.getEntityById(request.getPropertyId());

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

    @Transactional(readOnly = true)
    public List<BookingResponse> searchMyBooking(int userId, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "startDate");

        List<BookingEntity> bookingEntityList = bookingRepository.findByUserEntityId(userId, sort);

        List<BookingResponse> responseList = BookingMapper.convertEntityToResponseList(bookingEntityList);

        return responseList;
    }

    @Transactional(readOnly = true)
    public boolean isBookingDateFree(int propertyId, LocalDate startDate, LocalDate endDate) {

        List<BookingEntity> existBookings = bookingRepository.findActiveByPropertyId(propertyId);

        for (BookingEntity bookingEntity : existBookings) {
            LocalDate startDateBooking = bookingEntity.getStartDate();
            LocalDate endDateBooking = bookingEntity.getEndDate();
            if ((endDate.isAfter(startDateBooking) && startDate.isBefore(endDateBooking))) {
                return false;
            }
        }
        return true;

    }

    private BookingEntity findBookingWithAccessCheck(int userId, int bookingId) {
        BookingEntity bookingEntity = getEntityById(bookingId);

        UserEntity userEntity = userService.getUserEntityById(userId).orElseThrow(() -> new UnauthorizedException("Session is invalid"));

        if (bookingEntity.getUserEntity().getId() != userId && userEntity.getRole() == Role.USER) {
            throw new AccessDeniedException("No access");
        }

        return bookingEntity;
    }

    @Transactional
    public void deleteBooking(int userId, int bookingId) {

       findBookingWithAccessCheck(userId, bookingId);

        bookingRepository.deleteById(bookingId);

        logger.info("Booking is deleted. User id: {}", userId);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(int userId, int bookingId) {
        BookingEntity bookingEntity = findBookingWithAccessCheck(userId, bookingId);

        BookingResponse response = BookingMapper.convertBookingEntityToResponse(bookingEntity);
        return response;
    }

    @Transactional(readOnly = true)
    public BookingEntity getEntityById(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
    }


}
