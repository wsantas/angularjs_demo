package org.playground.admin.service;

import org.playground.admin.model.Authority;

import java.util.List;

/**
 * Created by wsantasiero on 9/25/14.
 */
public interface AuthorityService {
    List<Authority> findAdminAuthorityByUserIdAndInstitutionId(String userId, String institutionId);
}
