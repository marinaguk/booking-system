package bookingApp.dto;

import bookingApp.entity.PropertyEntity;
import bookingApp.entity.UserEntity;

public class AddPropertyRequest {

    private String name;
    private String city;
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
