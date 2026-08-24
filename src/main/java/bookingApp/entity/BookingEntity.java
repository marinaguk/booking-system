package bookingApp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof BookingEntity)) return false;

        BookingEntity that = (BookingEntity) o;
        return id == that.getId() &&
        Objects.equals(userEntity, that.getUserEntity()) &&
        Objects.equals(propertyEntity, that.getPropertyEntity()) &&
        Objects.equals(startDate, that.getStartDate()) &&
        Objects.equals(endDate, that.getEndDate());

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userEntity.getId(), propertyEntity.getPropertyId(), startDate, endDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public PropertyEntity getPropertyEntity() {
        return propertyEntity;
    }

    public void setPropertyEntity(PropertyEntity propertyEntity) {
        this.propertyEntity = propertyEntity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
