package bookingApp.service;

import bookingApp.entity.UserEntity;
import bookingApp.exception.BadRequestException;
import bookingApp.repository.UserRepository;
import bookingApp.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public int register(String name, String password) {
        if (userRepository.findByName(name).isPresent()) {
            throw new BadRequestException("User already exists");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        UserEntity userEntity = new UserEntity(name, hashedPassword);
        userRepository.save(userEntity);

        int userId = userEntity.getId();

        logger.info("User is registered. User id: {}", userId);
        return userId;
    }

    @Transactional(readOnly = true)
    public String login(String name, String password) {
        UserEntity user = userRepository.findByName(name).orElse(null);

        if (user == null) {
            return null;
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }

        return SessionManager.createSession(user.getId());
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserEntityById(Integer id) {
        return userRepository.findById(id);
    }


}
