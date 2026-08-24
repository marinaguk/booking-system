package bookingApp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class GsonUtil {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    LocalDate.class,
                    new LocalDateAdapter()
            )
            .setPrettyPrinting()
            .create();
}
