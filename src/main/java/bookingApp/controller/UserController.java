package bookingApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bookingApp.dto.*;
import bookingApp.entity.UserEntity;
import bookingApp.exception.*;
import bookingApp.service.*;
import bookingApp.util.HttpUtil;
import bookingApp.util.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

import static bookingApp.util.GsonUtil.messageToJson;
import static bookingApp.util.HttpUtil.getIdFromRequest;
import static bookingApp.util.HttpUtil.getUserIdAuthorization;
import static bookingApp.util.ResponseUtil.sendResponse;

import static bookingApp.util.GsonUtil.gson;

public class UserController implements HttpHandler {

    private final UserService userService;
    private final PropertyService propertyService;

    public UserController(UserService userService, PropertyService propertyService) {
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException { //получает данные запроса, заголовки, тело и отправляет ответ клиенту

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            switch (path) {

                case "/user/register":
                    if ("POST".equals(method)) {
                        handleRegister(exchange);
                    } else {
                        throw new MethodNotAllowedException("Method Not Allowed", "POST");
                    }
                    break;

                case "/user/login":
                    if ("POST".equals(method)) {
                        handleLogin(exchange);
                    } else {
                        throw new MethodNotAllowedException("Method Not Allowed", "POST");
                    }
                    break;

                case "/user/property":
                    if ("POST".equals(method)) {
                        handleAddProperty(exchange);
                    } else {
                        throw new MethodNotAllowedException("Method Not Allowed", "POST");
                    }
                    break;

                case "/user":
                    if ("GET".equals(method)) {
                       handleGet(exchange);
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

    private void handleRegister(HttpExchange exchange) throws IOException {
        String body = HttpUtil.readBody(exchange);

        RegisterRequest request = gson.fromJson(body, RegisterRequest.class); //преобразуем строку JSON в объект
        ValidationUtil.requireNotNull(request, "Invalid JSON");

        ValidationUtil.requireNotEmpty(request.getName(), "Name is required");
        ValidationUtil.requireNotEmpty(request.getPassword(), "Password is required");

        int userId = userService.register(request.getName(), request.getPassword());


        String url = "/user?id=" + userId;

        sendResponse(exchange,201, messageToJson("User is registered"), Map.of("Location", url));
    }

    private void handleLogin(HttpExchange exchange) throws IOException{
        String body = HttpUtil.readBody(exchange);
        LoginRequest request = gson.fromJson(body, LoginRequest.class);
        ValidationUtil.requireNotNull(request, "Invalid JSON");

        ValidationUtil.requireNotEmpty(request.getName(), "Name is required");
        ValidationUtil.requireNotEmpty(request.getPassword(), "Password is required");

        String sessionId = userService.login(request.getName(), request.getPassword());

        if (sessionId == null) {
            throw new UnauthorizedException("Invalid credentials");
        }

        sendResponse(exchange, 200, gson.toJson(Map.of("sessionId", sessionId)));
    }

    private void handleAddProperty(HttpExchange exchange) throws IOException {

        int userId = getUserIdAuthorization(exchange);

        String body = HttpUtil.readBody(exchange);
        AddPropertyRequest addPropertyRequest = gson.fromJson(body, AddPropertyRequest.class);
        ValidationUtil.requireNotNull(addPropertyRequest, "Invalid JSON");
        ValidationUtil.requireNotNull(addPropertyRequest.getName(), "Name is required");
        ValidationUtil.requireNotNull(addPropertyRequest.getCity(), "City is required");
        ValidationUtil.requireNotNull(addPropertyRequest.getPrice(), "Price is required");

        UserEntity userEntity = userService.getUserEntityById(userId);

        if (userEntity == null) {
            throw new UnauthorizedException("Session is invalid");
        }

        int propertyId = propertyService.addProperty(userEntity, addPropertyRequest);

        String url = "/property?id=" + propertyId;

        sendResponse(exchange, 201, messageToJson("Property is added"), Map.of("Location", url));
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        int userId = getIdFromRequest(exchange);

        UserEntity userEntity = userService.getUserEntityById(userId);


        if (userEntity == null) {
            throw new NotFoundException("User not found");
        }

        UserResponse userResponse = new UserResponse(userId, userEntity.getName());

        String response = gson.toJson(userResponse);

        sendResponse(exchange, 200, response);
    }
}
