package bookingApp.util;

import bookingApp.controller.PropertyController;
import bookingApp.exception.*;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static bookingApp.util.ResponseUtil.sendResponse;

public class ExceptionHandlerUtil {

    private static final Logger logger =
            LoggerFactory.getLogger(ExceptionHandlerUtil.class);

    public static void handle(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof BadRequestException) {
            sendResponse(exchange, 400, e.getMessage());
            logger.error("BadRequestException", e);
        } else if (e instanceof UnauthorizedException) {
            sendResponse(exchange, 401, e.getMessage());
            logger.error("UnauthorizedException", e);
        } else if (e instanceof AccessDeniedException) {
            sendResponse(exchange, 403, e.getMessage());
            logger.error("AccessDeniedException", e);
        } else if (e instanceof NotFoundException) {
            sendResponse(exchange, 404, e.getMessage());
            logger.error("NotFoundException", e);
        } else if (e instanceof MethodNotAllowedException) {
            sendResponse(exchange, 405, e.getMessage(), Map.of("Allow", ((MethodNotAllowedException) e).getAllowedMethods()));
            logger.error("MethodNotAllowedException", e);
        } else {
            sendResponse(exchange, 500, "Internal Server Error");
            logger.error("Exception", e);
        }
    }
}
