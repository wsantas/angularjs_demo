package org.playground.admin.service;

import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 * User: jfitzgerald
 * Date: 10/3/14
 * Time: 3:52 PM
 */
public interface SortingService {
    Sort.Direction getSortDirectionFromString(String sortOrder);
}
