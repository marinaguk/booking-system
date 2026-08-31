package bookingApp.controller;

import bookingApp.dto.*;
import bookingApp.exception.MethodNotAllowedException;
import bookingApp.exception.NotFoundException;
import bookingApp.service.*;
import bookingApp.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bookingApp.util.GsonUtil.*;
import static bookingApp.util.HttpUtil.*;
import static bookingApp.util.ResponseUtil.sendResponse;

public class BookingController implements HttpHandler {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            switch (path) {

                case "/booking":
                    if ("POST".equals(method)) {
                        handleCreate(exchange);
                    } else if ("DELETE".equals(method)) {
                        handleDelete(exchange);
                    } else if ("GET".equals(method)) {
                        handleGetBookingById(exchange);
                    } else {
                        throw new MethodNotAllowedException("Method Not Allowed", "GET, POST, DELETE");
                    }
                    break;

                case "/booking/my":
                    if ("GET".equals(method)) {
                        handleSearchMyBooking(exchange);
                    } else {
                        throw new MethodNotAllowedException("Method Not Allowed", "GET");
                    }
                    break;

                default:
                    throw new NotFoundException("Not found");

            }
        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        } finally {
            exchange.close();
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        int userId = getUserIdAuthorization(exchange);

        String body = HttpUtil.readBody(exchange);
        CreateBookingRequest request = gson.fromJson(body, CreateBookingRequest.class);

        ValidationUtil.requireNotNull(request, "Invalid JSON");
        ValidationUtil.requirePositive(request.getPropertyId(), "Property id is required");

        ValidationUtil.requireNotNull(request.getStartDate(), "Start date id is required");
        ValidationUtil.requireNotNull(request.getEndDate(), "End date id is required");

        int bookingId = bookingService.createBooking(userId, request);

        String url = "/booking?id=" + bookingId;

        sendResponse(exchange, 201, messageToJson("Booking created"), Map.of("Location", url));
    }

    private void handleSearchMyBooking(HttpExchange exchange) throws IOException {
        int userId = getUserIdAuthorization(exchange);

        String sortBy = HttpUtil.getStringQueryParam(exchange, "sort", "date");
        String sortDirection = HttpUtil.getStringQueryParam(exchange, "direction", "asc");

        ValidationUtil.requireValidBookingSort(sortBy, sortDirection);

        List<BookingResponse> responseList = bookingService.searchMyBooking(userId, sortBy, sortDirection);

        String response = gson.toJson(responseList);
        sendResponse(exchange, 200, response);
    }

    private void handleDelete (HttpExchange exchange) throws IOException{
        int userId = getUserIdAuthorization(exchange);

        int bookingId = getIdFromRequest(exchange);

        bookingService.deleteBooking(userId, bookingId);
        sendResponse(exchange, 204);
    }

    private void handleGetBookingById(HttpExchange exchange) throws IOException {
        int userId = getUserIdAuthorization(exchange);
        int bookingId = getIdFromRequest(exchange);

        BookingResponse bookingResponse = bookingService.getBookingById(userId, bookingId);

        String response = gson.toJson(bookingResponse);
        sendResponse(exchange, 200, response);
    }
}
