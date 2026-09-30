package bookingApp.repository;

import bookingApp.dto.SearchPropertyRequest;
import bookingApp.model.SearchPropertyResult;

public interface PropertyRepositoryCustom {
        SearchPropertyResult search(SearchPropertyRequest request, int offset, int size,
                                    String sortBy, String sortDirection);
}
