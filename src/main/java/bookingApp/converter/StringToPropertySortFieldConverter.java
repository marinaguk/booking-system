package bookingApp.converter;

import bookingApp.model.PropertySortField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPropertySortFieldConverter implements Converter<String, PropertySortField> {
    @Override
    public PropertySortField convert(String source) {
        return PropertySortField.valueOf(source.toUpperCase());
    }
}
