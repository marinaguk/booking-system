package bookingApp.util;

import bookingApp.exception.BadRequestException;
import bookingApp.exception.UnauthorizedException;
import com.sun.net.httpserver.HttpExchange;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpUtil {

    public static String readBody(HttpExchange exchange) throws IOException {

        InputStream inputStream = exchange.getRequestBody();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int nRead;

        while ((nRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toString();
    }

    public static Integer getUserIdFromSession(HttpExchange exchange) {
        String session = exchange.getRequestHeaders().getFirst("Session-Id");
        if (session == null) return null;
        return SessionManager.getUserId(session);
    }

    public static int getUserIdAuthorization(HttpExchange exchange) {
        Integer userId = getUserIdFromSession(exchange);

        if (userId == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        return userId;
    }

    public static int getIdFromRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            throw new BadRequestException("Id is required");
        }

        String[] params = query.split("&");

        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("id")) {
                try {
                    return Integer.parseInt(pair[1]);
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Invalid id");
                }
            }
        }

        throw new BadRequestException("Invalid id");
    }

    public static int getIntQueryParam(HttpExchange exchange, String name, int defaultValue) {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            return defaultValue;
        }

        String[] params = query.split("&");

        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(name)) {
                try {
                    return Integer.parseInt(pair[1]);
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Invalid number");
                }
            }
        }

        return defaultValue;
    }

    public static String getStringQueryParam(HttpExchange exchange, String name, String defaultValue) {
        String query = exchange.getRequestURI().getQuery();

        if (query == null) {
            return defaultValue;
        }

        String[] params = query.split("&");

        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(name)) {
                return pair[1];
            }
        }

        return defaultValue;

    }

}
