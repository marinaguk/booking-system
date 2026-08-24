package bookingApp.service;

import bookingApp.controller.PropertyController;
import bookingApp.dto.AddPropertyRequest;
import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import bookingApp.exception.BadRequestException;
import bookingApp.repository.UserRepository;
import bookingApp.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(String name, String password) {

        if (userRepository.findByName(name) != null) {
            throw new BadRequestException("User already exists");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        UserEntity userEntity = new UserEntity(name, hashedPassword);
        userRepository.save(userEntity);
        logger.info("User is registered");
    }

    public String login(String name, String password) {

        UserEntity user = userRepository.findByName(name);

        if (user == null) {
            return null;
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }

        return SessionManager.createSession(user.getId());
    }


    public UserEntity getUserEntityById(Integer id) {

        if (id == null) {
            return null;
        }

        UserEntity userEntity = userRepository.findById(id);
        return userEntity;

    }

}
