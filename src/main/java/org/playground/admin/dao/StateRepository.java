package org.playground.admin.dao;

import org.playground.admin.model.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wsantasiero on 9/22/14.
 */
public interface StateRepository extends CrudRepository<State, Integer> {
    List<State> findStatesByCountryCodeOrderByNameAsc(String code);
}
