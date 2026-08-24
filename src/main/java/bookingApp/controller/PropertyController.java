package bookingApp.controller;

import bookingApp.dto.*;
import bookingApp.service.PropertyService;
import bookingApp.util.ExceptionHandlerUtil;
import bookingApp.util.HttpUtil;

import bookingApp.util.ValidationUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static bookingApp.util.HttpUtil.*;
import static bookingApp.util.ResponseUtil.sendResponse;

import static bookingApp.util.GsonUtil.gson;

public class PropertyController implements HttpHandler {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if ("POST".equals(exchange.getRequestMethod()) &&
                    "/property/search".equals(exchange.getRequestURI().getPath())) {
                handleSearch(exchange);
            } else if ("DELETE".equals(exchange.getRequestMethod()) &&
                    "/property/delete".equals(exchange.getRequestURI().getPath())) {
                handleDelete(exchange);
            } else if ("GET".equals(exchange.getRequestMethod()) &&
                    "/property/getavailability".equals(exchange.getRequestURI().getPath())) {
                handleGetAvailability(exchange);
            } else if ("GET".equals(exchange.getRequestMethod()) &&
                    "/property/getallbookings".equals(exchange.getRequestURI().getPath())) {
                handleGetAllBooking(exchange);
            }
        } catch (Exception e) {
            ExceptionHandlerUtil.handle(exchange, e);
        } finally {
            exchange.close();
        }
    }

    public void handleSearch(HttpExchange exchange) throws IOException{
        String body = HttpUtil.readBody(exchange);
        SearchPropertyRequest request = gson.fromJson(body, SearchPropertyRequest.class);

        int page = HttpUtil.getIntQueryParam(exchange, "page", 1);
        int size = HttpUtil.getIntQueryParam(exchange, "size", 5);

        ValidationUtil.requireNotNull(request, "Invalid JSON");
        ValidationUtil.requireStartAndEndDate(request.getStartDate(), request.getEndDate());
        ValidationUtil.requireValidPriceRange(request.getMinPrice(), request.getMaxPrice());
        ValidationUtil.requireValidPageSize(page, size);

        String sortBy = HttpUtil.getStringQueryParam(exchange, "sort", "name");
        String sortDirection = HttpUtil.getStringQueryParam(exchange, "sortDirection", "asc");

        ValidationUtil.requireValidPropertySort(sortBy, sortDirection);

        SearchPropertyResponse searchPropertyResponse = propertyService.search(request, page, size, sortBy, sortDirection);

        String response = gson.toJson(searchPropertyResponse);
        sendResponse(exchange, 200, response);
    }

    public void handleDelete(HttpExchange exchange) throws IOException{
        int userId = getUserIdAuthorization(exchange);

        int propertyId = getIdFromRequest(exchange);

        propertyService.delete(userId, propertyId);
        sendResponse(exchange, 200, "Property deleted");
    }

    public void handleGetAvailability(HttpExchange exchange) throws IOException {

        int propertyId = getIdFromRequest(exchange);

       PropertyAvailabilityResponse availabilityResponse = propertyService.getAvailability(propertyId);

       String response = gson.toJson(availabilityResponse);

       sendResponse(exchange, 200, response);
    }

    public void handleGetAllBooking(HttpExchange exchange) throws IOException {
        int userId = getUserIdAuthorization(exchange);
        int propertyId = getIdFromRequest(exchange);

        int page = HttpUtil.getIntQueryParam(exchange, "page", 1);
        int size = HttpUtil.getIntQueryParam(exchange, "size", 5);
        ValidationUtil.requireValidPageSize(page, size);

        AllBookingResponse allBookingResponse = propertyService.getAllBooking(userId, propertyId, page, size);

        String response = gson.toJson(allBookingResponse);

        sendResponse(exchange, 200, response);
    }
}
