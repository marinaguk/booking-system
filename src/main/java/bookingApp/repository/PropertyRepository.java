package bookingApp.repository;

import bookingApp.dto.SearchPropertyRequest;
import bookingApp.entity.BookingEntity;
import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import bookingApp.exception.BadRequestException;
import bookingApp.exception.NotFoundException;
import bookingApp.model.SearchPropertyResult;
import bookingApp.util.JpaUtil;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PropertyRepository {

    public void save(PropertyEntity propertyEntity) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            UserEntity managedUser = entityManager.merge(propertyEntity.getOwner());
            propertyEntity.setOwner(managedUser); // “перепривязываем” owner к текущему EntityManager

            entityManager.persist(propertyEntity);
            entityTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityTransaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }

    }

    public List<PropertyEntity> findAll() {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "FROM PropertyEntity";

            TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(request, PropertyEntity.class);

            return typedQuery.getResultList();
        }  finally {
            entityManager.close();
        }

    }

    public PropertyEntity findByName(String name) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT p FROM PropertyEntity p WHERE p.propertyName = :name";

            TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(request, PropertyEntity.class);

            typedQuery.setParameter("name", name);

            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

    public PropertyEntity findById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT p FROM PropertyEntity p WHERE p.propertyId = :id";

            TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(request, PropertyEntity.class);

            typedQuery.setParameter("id", id);

            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

    public List<PropertyEntity> findByCity(String city) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT p FROM PropertyEntity p WHERE p.propertyCity = :city";

            TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(request, PropertyEntity.class);

            typedQuery.setParameter("city", city);

            return typedQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

    public void deleteById(int id) {

        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            String request = "DELETE FROM PropertyEntity b WHERE b.propertyId = :id";

            Query query = entityManager.createQuery(request);

            int deleted = query.setParameter("id", id).executeUpdate();

            if (deleted == 0) {
                throw new NotFoundException("Property not found");
            }
            tx.commit();
        } catch (NoResultException e) {
            tx.rollback();
            throw e;
        } finally {
            entityManager.close();
        }

    }

    public List<PropertyEntity> findAllPaged(int offset, int size) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "FROM PropertyEntity";

            TypedQuery<PropertyEntity> typedQuery = entityManager.createQuery(request, PropertyEntity.class);

            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(size);

            return typedQuery.getResultList();
        }  finally {
            entityManager.close();
        }
    }

    public SearchPropertyResult search(SearchPropertyRequest request, int offset, int size, String sortBy, String sortDirection) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        SearchPropertyResult searchResult = new SearchPropertyResult();

        StringBuilder itemsJpql =
                new StringBuilder(
                        "SELECT p FROM PropertyEntity p WHERE 1=1"
                );

        if (request.getCity() != null) {
            itemsJpql.append(" AND p.propertyCity = :city");
        }

        if (request.getMinPrice() != null) {
            itemsJpql.append(" AND p.propertyPrice >= :minPrice");
        }

        if (request.getMaxPrice() != null) {
            itemsJpql.append(" AND p.propertyPrice <= :maxPrice");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            itemsJpql.append(" AND NOT EXISTS (\n" +
                    "    SELECT b\n" +
                    "    FROM BookingEntity b\n" +
                    "    WHERE b.propertyEntity = p\n" +
                    "    AND :startDate <= b.endDate\n" +
                    "    AND :endDate >= b.startDate\n" +
                    ")");
        }

        String sortField;

        switch (sortBy) {
            case "price":
                sortField = "p.propertyPrice";
                break;
            case "name":
                sortField = "p.propertyName";
                break;
            default:
                throw new BadRequestException("Invalid sort field");
        }

        String countRequest = itemsJpql.toString().replace("SELECT p", "SELECT COUNT(p)");

        itemsJpql.append(" ORDER BY " + sortField + " " + sortDirection.toUpperCase());

        String itemsRequest = itemsJpql.toString();

        try {

            TypedQuery<PropertyEntity> itemsTypedQuery = entityManager.createQuery(itemsRequest, PropertyEntity.class);
            TypedQuery<Long> countTypedQuery = entityManager.createQuery(countRequest, Long.class);

            if (request.getCity() != null) {
                itemsTypedQuery.setParameter("city", request.getCity());
                countTypedQuery.setParameter("city", request.getCity());
            }

            if (request.getMinPrice() != null) {
                itemsTypedQuery.setParameter("minPrice", request.getMinPrice());
                countTypedQuery.setParameter("minPrice", request.getMinPrice());
            }

            if (request.getMaxPrice() != null) {
                itemsTypedQuery.setParameter("maxPrice", request.getMaxPrice());
                countTypedQuery.setParameter("maxPrice", request.getMaxPrice());
            }

            if (request.getStartDate() != null && request.getEndDate() != null) {
                LocalDate startDate = request.getStartDate();
                LocalDate endDate = request.getEndDate();
                itemsTypedQuery.setParameter("startDate", startDate);
                itemsTypedQuery.setParameter("endDate", endDate);
                countTypedQuery.setParameter("startDate", startDate);
                countTypedQuery.setParameter("endDate", endDate);
            }

            itemsTypedQuery.setFirstResult(offset);
            itemsTypedQuery.setMaxResults(size);

            searchResult.setItems(itemsTypedQuery.getResultList());
            searchResult.setTotalItems(countTypedQuery.getSingleResult());

            return searchResult;
        }  finally {
            entityManager.close();
        }
    }

}
