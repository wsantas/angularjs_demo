package org.playground.admin.dao;

import org.playground.admin.model.ApplicationProperty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wsantasiero on 7/3/14.
 */
public interface ApplicationPropertiesRepository extends CrudRepository<ApplicationProperty, Long> {

    public List<ApplicationProperty> findAll();

}
