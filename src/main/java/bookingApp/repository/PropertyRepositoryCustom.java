package bookingApp.repository;

import bookingApp.dto.SearchPropertyRequest;
import bookingApp.model.PropertySortField;
import bookingApp.model.SearchPropertyResult;
import bookingApp.model.SortDirection;

public interface PropertyRepositoryCustom {

        SearchPropertyResult search(SearchPropertyRequest request, int offset, int size,
                                    PropertySortField sortBy, SortDirection sortDirection);

}
