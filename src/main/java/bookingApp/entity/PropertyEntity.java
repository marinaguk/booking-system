package bookingApp.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "property")
public class PropertyEntity {

    public PropertyEntity() {}

    public PropertyEntity(int id, String name, String city, double price, UserEntity owner) {
        this.propertyId = id;
        this.propertyName = name;
        this.propertyCity = city;
        this.propertyPrice = price;
        this.owner = owner;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private int propertyId;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_city")
    private String propertyCity;

    @Column(name = "property_price")
    private double propertyPrice;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;


    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyCity() {
        return propertyCity;
    }

    public void setPropertyCity(String propertyCity) {
        this.propertyCity = propertyCity;
    }

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof PropertyEntity)) return false;

        return propertyId == ((PropertyEntity) obj).getPropertyId();
    }

    @Override
    public int hashCode() {
        return propertyId;
    }
}
