package bookingApp;

import bookingApp.controller.BookingController;
import bookingApp.controller.PropertyController;
import bookingApp.controller.UserController;
import bookingApp.repository.BookingRepository;
import bookingApp.repository.PropertyRepository;
import bookingApp.repository.UserRepository;
import bookingApp.service.BookingService;
import bookingApp.service.PropertyService;
import bookingApp.service.UserService;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;

public class Main {

    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        BookingRepository bookingRepository = new BookingRepository();
        PropertyRepository propertyRepository = new PropertyRepository();
        UserRepository userRepository = new UserRepository();

        UserService userService = new UserService(userRepository);
        BookingService bookingService = new BookingService(propertyRepository, bookingRepository, userService);
        PropertyService propertyService = new PropertyService(propertyRepository, userRepository, bookingRepository);

        UserController userController = new UserController(userService, propertyService);
        BookingController bookingController = new BookingController(bookingService);
        PropertyController propertyController = new PropertyController(propertyService);

        server.createContext("/user", userController);
        server.createContext("/booking", bookingController);
        server.createContext("/property", propertyController);

        server.start();

        logger.info("Server started on http://localhost:8080");

    }

}
