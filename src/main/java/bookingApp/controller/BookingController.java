package bookingApp.controller;

import bookingApp.dto.*;
import bookingApp.service.*;
import bookingApp.util.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import static bookingApp.util.ValidationUtil.requireValidSortDirection;

@RestController
@RequestMapping("/booking")
public class BookingController{

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestHeader(value = "Session-Id", required = false) String sessionId,
                                                      @Valid @RequestBody CreateBookingRequest createBookingRequest) {

        int userId = ValidationUtil.requireValidSessionId(sessionId);

        int bookingId = bookingService.createBooking(userId, createBookingRequest);

        return ResponseEntity.created(URI.create("/booking?id="+bookingId)).body(Map.of("message", "Booking Created"));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> searchMyBooking(@RequestHeader(value = "Session-Id", required = false) String sessionId,
                                                                 @RequestParam(defaultValue = "asc") String sortDirection) {
        int userId = ValidationUtil.requireValidSessionId(sessionId);
        requireValidSortDirection(sortDirection);
        List<BookingResponse> responseList = bookingService.searchMyBooking(userId, sortDirection);

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader(value = "Session-Id", required = false) String sessionId,
                                       @RequestParam int id) {
        int userId = ValidationUtil.requireValidSessionId(sessionId);
        bookingService.deleteBooking(userId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BookingResponse> getById (@RequestHeader(value = "Session-Id", required = false) String sessionId,
                                                    @RequestParam int id) {
        int userId = ValidationUtil.requireValidSessionId(sessionId);
        BookingResponse bookingResponse = bookingService.getBookingById(userId, id);
        return ResponseEntity.ok(bookingResponse);
    }
}
