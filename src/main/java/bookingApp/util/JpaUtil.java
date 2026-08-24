package bookingApp.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("my-persistence-unit");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
