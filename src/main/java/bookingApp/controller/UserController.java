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
        try {
            if ("POST".equals(exchange.getRequestMethod()) &&
                    "/user/register".equals(exchange.getRequestURI().getPath())) {

                handleRegister(exchange);

            } else if ("POST".equals(exchange.getRequestMethod()) &&
                    "/user/login".equals(exchange.getRequestURI().getPath())){

                handleLogin(exchange);

            } else if ("POST".equals(exchange.getRequestMethod()) &&
                    "/user/addproperty".equals(exchange.getRequestURI().getPath())) {

                handleAddProperty(exchange);

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

        userService.register(request.getName(), request.getPassword());
        sendResponse(exchange,200, "User is registered");
    }

    private void handleLogin(HttpExchange exchange) throws IOException{
        String body = HttpUtil.readBody(exchange);
        LoginRequest request = gson.fromJson(body, LoginRequest.class);
        ValidationUtil.requireNotNull(request, "Invalid JSON");

        ValidationUtil.requireNotEmpty(request.getName(), "Name is required");
        ValidationUtil.requireNotEmpty(request.getPassword(), "Password is required");

        String sessionId = userService.login(request.getName(), request.getPassword());

        ValidationUtil.requireNotNull(sessionId, "Invalid credentials");
        sendResponse(exchange, 200, sessionId);
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
            throw new NotFoundException("User not found");
        }

        propertyService.addProperty(userEntity, addPropertyRequest);
        sendResponse(exchange, 200, "Property is added");
    }
}
