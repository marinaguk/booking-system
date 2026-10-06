package bookingApp.controller;

import bookingApp.dto.*;
import bookingApp.model.SortDirection;
import bookingApp.service.*;
import bookingApp.util.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController{

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody CreateBookingRequest createBookingRequest) {

        int userId = Integer.parseInt(jwt.getSubject());

        int bookingId = bookingService.createBooking(userId, createBookingRequest);

        return ResponseEntity.created(URI.create("/booking/" + bookingId)).body(Map.of("message", "Booking Created"));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> searchMyBooking(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestParam(defaultValue = "ASC") SortDirection sortDirection) {
        int userId = Integer.parseInt(jwt.getSubject());
        List<BookingResponse> responseList = bookingService.searchMyBooking(userId, sortDirection);

        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable int id) {
        int userId = Integer.parseInt(jwt.getSubject());
        bookingService.deleteBooking(userId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById (@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable int id) {
        int userId = Integer.parseInt(jwt.getSubject());
        BookingResponse bookingResponse = bookingService.getBookingById(userId, id);
        return ResponseEntity.ok(bookingResponse);
    }
}
