package bookingApp.controller;

import bookingApp.dto.*;

import bookingApp.service.PropertyService;

import bookingApp.util.ValidationUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/property")
public class PropertyController{

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchPropertyResponse> search(@RequestParam(defaultValue = "1")  @Min(value = 1, message = "Page must be positive") int page,
                                                         @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be positive") @Max(value = 100, message = "Max size is 100") int size,
                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDirection,
                                                         @Valid @RequestBody SearchPropertyRequest searchPropertyRequest) {

        ValidationUtil.requireStartAndEndDate(searchPropertyRequest.getStartDate(), searchPropertyRequest.getEndDate());
        ValidationUtil.requireValidPriceRange(searchPropertyRequest.getMinPrice(), searchPropertyRequest.getMaxPrice());
        ValidationUtil.requireValidPropertySort(sortBy, sortDirection);

        SearchPropertyResponse searchPropertyResponse = propertyService.search(searchPropertyRequest, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(searchPropertyResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam int id,
                                       @RequestHeader(value = "Session-Id", required = false) String sessionId) {

        int userId = ValidationUtil.requireValidSessionId(sessionId);

        propertyService.delete(userId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<PropertyAvailabilityResponse> getAvailability(@RequestParam int id) {

        PropertyAvailabilityResponse availabilityResponse = propertyService.getAvailability(id);
        return ResponseEntity.ok(availabilityResponse);
    }

    @GetMapping("/allbookings")
    public ResponseEntity<AllBookingResponse> getAllBooking(@RequestParam int id,
                                                            @RequestHeader(value = "Session-Id", required = false) String sessionId,
                                                            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page must be positive") int page,
                                                            @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be positive") @Max(value = 100, message = "Max size is 100") int size){
        int userId = ValidationUtil.requireValidSessionId(sessionId);
        AllBookingResponse response = propertyService.getAllBooking(userId, id, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PropertyResponse> getById(@RequestParam int id) {
        PropertyResponse propertyResponse = propertyService.getResponseById(id);
        return ResponseEntity.ok(propertyResponse);
    }

}
