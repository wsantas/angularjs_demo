package org.playground.admin.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: jfitzgerald
 * Date: 10/3/14
 * Time: 3:52 PM
 */
@Service
public class SortingServiceImpl implements SortingService {

    @Override
    public Sort.Direction getSortDirectionFromString(String sortOrder) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if(sortOrder.equals("ASC")) {
            sortDirection = Sort.Direction.ASC;
        } else if(sortOrder.equals("DESC")) {
            sortDirection = Sort.Direction.DESC;
        }
        return sortDirection;
    }
}
