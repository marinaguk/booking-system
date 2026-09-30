package bookingApp.repository;

import bookingApp.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    @Query("SELECT b FROM BookingEntity b " +
            "WHERE b.propertyEntity.propertyId = :id AND b.endDate >= CURRENT_DATE " +
            "ORDER BY b.startDate")
    List<BookingEntity> findActiveByPropertyId(@Param("id") int id);

    List<BookingEntity> findByUserEntityId(Integer id, Sort sort);

    Page<BookingEntity> findByPropertyEntityPropertyId(Integer id, Pageable pageable);


}
