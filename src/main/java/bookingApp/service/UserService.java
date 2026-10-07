package bookingApp.service;

import bookingApp.entity.UserEntity;
import bookingApp.exception.BadRequestException;
import bookingApp.exception.UnauthorizedException;
import bookingApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public int register(String name, String password) {
        if (userRepository.findByName(name).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity(name, hashedPassword);
        userRepository.save(userEntity);

        int userId = userEntity.getId();

        logger.info("User is registered. User id: {}", userId);
        return userId;
    }

    @Transactional(readOnly = true)
    public String login(String name, String password) {
        UserEntity user = userRepository.findByName(name)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return tokenService.generateToken(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserEntityById(Integer id) {
        return userRepository.findById(id);
    }


}
