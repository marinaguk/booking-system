package bookingApp.dto;

import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddPropertyRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    public PropertyEntity propertyRequestToEntity(UserEntity userEntity) {
        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setPropertyName(this.name);
        propertyEntity.setPropertyCity(this.city);
        propertyEntity.setPropertyPrice(this.price);
        propertyEntity.setOwner(userEntity);
        return propertyEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
