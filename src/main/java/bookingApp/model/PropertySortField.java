package bookingApp.model;

public enum PropertySortField {
    NAME("propertyName"),
    PRICE("propertyPrice");

    private final String fieldName;

    PropertySortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
