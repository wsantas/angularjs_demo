package org.playground.admin.dao;

import org.playground.admin.model.UserInstitutionProfile;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by wsantasiero on 10/13/14.
 */
public interface UserInstitutionProfileRepository extends CrudRepository<UserInstitutionProfile, Integer> {
    UserInstitutionProfile findByUserIdAndInstitutionId(int userId, int institutionId);
}
