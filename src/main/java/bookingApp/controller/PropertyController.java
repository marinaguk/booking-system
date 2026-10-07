package bookingApp.controller;

import bookingApp.dto.*;

import bookingApp.model.PropertySortField;
import bookingApp.model.SortDirection;
import bookingApp.service.PropertyService;

import bookingApp.util.ValidationUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
                                                         @RequestParam(defaultValue = "NAME") PropertySortField sortBy,
                                                         @RequestParam(defaultValue = "ASC") SortDirection sortDirection,
                                                         @Valid @RequestBody SearchPropertyRequest searchPropertyRequest) {

        ValidationUtil.requireStartAndEndDate(searchPropertyRequest.getStartDate(), searchPropertyRequest.getEndDate());
        ValidationUtil.requireValidPriceRange(searchPropertyRequest.getMinPrice(), searchPropertyRequest.getMaxPrice());

        SearchPropertyResponse searchPropertyResponse = propertyService.search(searchPropertyRequest, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(searchPropertyResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id,
                                       @AuthenticationPrincipal Jwt jwt) {

        int userId = Integer.parseInt(jwt.getSubject());

        propertyService.delete(userId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<PropertyAvailabilityResponse> getAvailability(@PathVariable int id) {

        PropertyAvailabilityResponse availabilityResponse = propertyService.getAvailability(id);
        return ResponseEntity.ok(availabilityResponse);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<AllBookingResponse> getAllBooking(@PathVariable int id,
                                                            @AuthenticationPrincipal Jwt jwt,
                                                            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page must be positive") int page,
                                                            @RequestParam(defaultValue = "5") @Min(value = 1, message = "Size must be positive") @Max(value = 100, message = "Max size is 100") int size){
        int userId = Integer.parseInt(jwt.getSubject());
        AllBookingResponse response = propertyService.getAllBooking(userId, id, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getById(@PathVariable int id) {
        PropertyResponse propertyResponse = propertyService.getResponseById(id);
        return ResponseEntity.ok(propertyResponse);
    }

}
