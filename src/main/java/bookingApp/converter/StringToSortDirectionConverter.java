package bookingApp.converter;

import bookingApp.model.SortDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortDirectionConverter implements Converter<String, SortDirection> {
    @Override
    public SortDirection convert(String source) {
        return  SortDirection.valueOf(source.toUpperCase());
    }
}
