package bookingApp.util;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static bookingApp.util.GsonUtil.messageToJson;

public class ResponseUtil {

    public static void sendResponse(HttpExchange exchange, int status, String message) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendResponse(HttpExchange exchange, int status) throws IOException {
        exchange.sendResponseHeaders(status, -1);
    }

    public static void sendResponse(HttpExchange exchange, int status, String message, Map<String, String> mapHeaders) throws IOException {

        Headers headers = exchange.getResponseHeaders();

        for (Map.Entry<String, String> entry : mapHeaders.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }

        sendResponse(exchange, status, message);
    }

}
