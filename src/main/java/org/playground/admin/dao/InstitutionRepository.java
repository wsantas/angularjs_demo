package org.playground.admin.dao;


import org.playground.admin.model.Institution;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jhahn on 9/3/14.
 */
public interface InstitutionRepository extends CrudRepository<Institution, Integer> {

    Institution findById(int id);

    Institution findByName(String name);

}

