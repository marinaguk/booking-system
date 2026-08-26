package bookingApp.repository;

import bookingApp.dto.AllInfoBookingResponse;
import bookingApp.entity.BookingEntity;
import bookingApp.exception.AppException;
import bookingApp.exception.BadRequestException;
import bookingApp.exception.NotFoundException;
import bookingApp.model.SearchBookingResult;
import bookingApp.util.JpaUtil;
import jakarta.persistence.*;

import java.util.List;

public class BookingRepository {

    public void save(BookingEntity bookingEntity) {

        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(bookingEntity);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException("Failed to save booking", e);
        } finally {
            entityManager.close();
        }

    }

    public BookingEntity findById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT b FROM BookingEntity b WHERE b.id = :id";

            TypedQuery<BookingEntity> typedQuery = entityManager.createQuery(request, BookingEntity.class);

            typedQuery.setParameter("id", id);

            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

    public List<BookingEntity> findByPropertyId(int id) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT b FROM BookingEntity b " +
                    "WHERE b.propertyEntity.propertyId = :id AND b.endDate >= CURRENT_DATE " +
                    "ORDER BY b.startDate";

            TypedQuery<BookingEntity> typedQuery = entityManager.createQuery(request, BookingEntity.class);
            typedQuery.setParameter("id", id);

            return typedQuery.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<BookingEntity> searchByUserId(int id, String sortBy, String sortDirection) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        StringBuilder jpql = new StringBuilder("SELECT b FROM BookingEntity b WHERE b.userEntity.id = :id");

        String sortField;

        if(sortBy.equals("date")) {
            sortField = "b.startDate";
        } else {
            throw new BadRequestException("Invalid sort field");
        }

        jpql.append(" ORDER BY " + sortField + " " + sortDirection.toUpperCase());

        try {
            String request = jpql.toString();

            TypedQuery<BookingEntity> typedQuery = entityManager.createQuery(request, BookingEntity.class);

            typedQuery.setParameter("id", id);

            return typedQuery.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void deleteById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            String request = "DELETE FROM BookingEntity b WHERE b.id = :id";

            Query query = entityManager.createQuery(request);

            int deleted = query.setParameter("id", id).executeUpdate();

            if (deleted == 0) {
                throw new NotFoundException("Booking not found");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof AppException) {
                throw e;
            }
            throw new RuntimeException("Failed to delete booking", e);
        } finally {
            entityManager.close();
        }

    }

    public SearchBookingResult getAllBookingByPropertyId(int propertyId, int offset, int size) {

        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        String request = "SELECT b " +
                "FROM BookingEntity b " +
                "WHERE b.propertyEntity.propertyId = :propertyId " +
                "ORDER BY b.startDate";

        String countRequest = "SELECT COUNT(b) " +
                "FROM BookingEntity b " +
                "WHERE b.propertyEntity.propertyId = :propertyId ";

        SearchBookingResult result = new SearchBookingResult();

        try {
            TypedQuery<BookingEntity> typedQuery = entityManager.createQuery(request, BookingEntity.class);
            TypedQuery<Long> countTypedQuery = entityManager.createQuery(countRequest, Long.class);

            typedQuery.setParameter("propertyId", propertyId);
            countTypedQuery.setParameter("propertyId", propertyId);

            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(size);

            result.setItems(typedQuery.getResultList());
            result.setTotalItems(countTypedQuery.getSingleResult());
            return result;
        } finally {
            entityManager.close();
        }

    }

}
