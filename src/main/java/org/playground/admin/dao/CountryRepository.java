package org.playground.admin.dao;

import org.playground.admin.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wsantasiero on 9/22/14.
 */
public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> findAllByOrderByNameAsc();
    Country findByCode(String code);
}
