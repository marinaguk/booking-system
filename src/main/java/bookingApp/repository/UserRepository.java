package bookingApp.repository;

import bookingApp.entity.UserEntity;
import bookingApp.util.JpaUtil;
import jakarta.persistence.*;

import java.util.List;

public class UserRepository {

    public void save(UserEntity userEntity) {

        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(userEntity);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }

    }

    public UserEntity findByName(String name) {

        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT u FROM UserEntity u WHERE u.name = :name";

            TypedQuery<UserEntity> typedQuery = entityManager.createQuery(request, UserEntity.class);

            typedQuery.setParameter("name", name);

            List<UserEntity> result = typedQuery.getResultList();

            if (result.isEmpty()) {
                return null;
            }

            return result.get(0);
            
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

    public UserEntity findById(int id) {
        EntityManager entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            String request = "SELECT u FROM UserEntity u WHERE u.id = :id";

            TypedQuery<UserEntity> typedQuery = entityManager.createQuery(request, UserEntity.class);

            typedQuery.setParameter("id", id);

            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }

    }

}
