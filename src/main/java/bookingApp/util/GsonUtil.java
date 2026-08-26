package bookingApp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.Map;

public class GsonUtil {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDate.class,
                    new LocalDateAdapter()
            )
            .setPrettyPrinting()
            .create();

    public static String messageToJson(String message) {

        if (message == null) {
            message = "";
        }

        return gson.toJson(Map.of("message", message));
    }

    public static String errorToJson(String error) {

        if (error == null) {
            error = "Unexpected error";
        }

        return gson.toJson(Map.of("error", error));
    }
}
