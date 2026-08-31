package bookingApp.util;

import bookingApp.exception.*;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static bookingApp.util.GsonUtil.errorToJson;
import static bookingApp.util.ResponseUtil.sendResponse;

public class ExceptionHandlerUtil {

    private static final Logger logger =
            LoggerFactory.getLogger(ExceptionHandlerUtil.class);

    public static void handle(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof BadRequestException) {
            logger.warn("BadRequestException", e);
            sendResponse(exchange, 400, errorToJson(e.getMessage()));
        } else if (e instanceof UnauthorizedException) {
            logger.warn("UnauthorizedException", e);
            sendResponse(exchange, 401, errorToJson(e.getMessage()));
        } else if (e instanceof AccessDeniedException) {
            logger.warn("AccessDeniedException", e);
            sendResponse(exchange, 403, errorToJson(e.getMessage()));
        } else if (e instanceof NotFoundException) {
            logger.warn("NotFoundException", e);
            sendResponse(exchange, 404, errorToJson(e.getMessage()));
        } else if (e instanceof MethodNotAllowedException) {
            logger.warn("MethodNotAllowedException", e);
            sendResponse(exchange, 405, errorToJson(e.getMessage()), Map.of("Allow", ((MethodNotAllowedException) e).getAllowedMethods()));
        } else {
            logger.error("Exception", e);
            sendResponse(exchange, 500, errorToJson("Internal Server Error"));
        }
    }
}
