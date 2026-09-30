package bookingApp.controller;

import bookingApp.dto.*;
import bookingApp.entity.UserEntity;
import bookingApp.exception.*;
import bookingApp.service.*;
import bookingApp.util.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

        String sessionId = userService.login(loginRequest.getName(), loginRequest.getPassword());

        if (sessionId == null) {throw new UnauthorizedException("Invalid credentials");}

        return ResponseEntity.ok(Map.of("sessionId", sessionId));
    }

    @PostMapping("/property")
    public ResponseEntity<Map<String, String>> addProperty(@RequestHeader(value = "Session-Id", required = false) String sessionId, @Valid @RequestBody AddPropertyRequest addPropertyRequest) {

        int userId = ValidationUtil.requireValidSessionId(sessionId);

        UserEntity userEntity = userService.getUserEntityById(userId).orElseThrow(()-> new UnauthorizedException("Session is invalid"));

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
