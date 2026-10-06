package bookingApp.controller;

import bookingApp.dto.AddPropertyRequest;
import bookingApp.dto.LoginRequest;
import bookingApp.dto.RegisterRequest;
import bookingApp.dto.UserResponse;
import bookingApp.entity.UserEntity;
import bookingApp.exception.NotFoundException;
import bookingApp.exception.UnauthorizedException;
import bookingApp.service.PropertyService;
import bookingApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController{

    private final UserService userService;
    private final PropertyService propertyService;

    public UserController(UserService userService, PropertyService propertyService) {
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        int userId = userService.register(registerRequest.getName(), registerRequest.getPassword());

        return ResponseEntity.created(URI.create("/user?id=" + userId))
                .body(Map.of("message", "User is registered"));

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {

        String token = userService.login(loginRequest.getName(), loginRequest.getPassword());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/property")
    public ResponseEntity<Map<String, String>> addProperty(@AuthenticationPrincipal Jwt jwt,
                                                           @Valid @RequestBody AddPropertyRequest addPropertyRequest) {

        int userId = Integer.parseInt(jwt.getSubject());

        UserEntity userEntity = userService.getUserEntityById(userId).orElseThrow(()-> new UnauthorizedException("User not found"));

        int propertyId = propertyService.addProperty(userEntity, addPropertyRequest);

        return ResponseEntity.created(URI.create("/property?id=" + propertyId)).body(Map.of("message", "Property is added"));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@RequestParam int id) {
        UserEntity userEntity = userService.getUserEntityById(id).orElseThrow(() -> new NotFoundException("User not found"));

        UserResponse userResponse = new UserResponse(id, userEntity.getName());

        return ResponseEntity.ok(userResponse);
    }

}
