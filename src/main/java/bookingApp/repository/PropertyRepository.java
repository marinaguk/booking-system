package bookingApp.repository;

import bookingApp.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Integer>, PropertyRepositoryCustom {

    Optional<PropertyEntity> findByPropertyName(String propertyName);

}
