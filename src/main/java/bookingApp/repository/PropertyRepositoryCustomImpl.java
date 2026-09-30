package bookingApp.repository;

import bookingApp.dto.SearchPropertyRequest;
import bookingApp.entity.PropertyEntity;
import bookingApp.exception.BadRequestException;
import bookingApp.model.SearchPropertyResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;

public class PropertyRepositoryCustomImpl implements PropertyRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public SearchPropertyResult search(SearchPropertyRequest request, int offset, int size, String sortBy, String sortDirection) {
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

    }
}
