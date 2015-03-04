package org.playground.admin.service;

import org.playground.admin.dao.AuthorityRepository;
import org.playground.admin.dao.UserInstitutionProfileRepository;
import org.playground.admin.model.Authority;
import org.playground.admin.model.UserInstitutionProfile;
import org.playground.admin.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wsantasiero on 9/25/14.
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserInstitutionProfileRepository userInstitutionProfileRepository;

    @Override
    public List<Authority> findAdminAuthorityByUserIdAndInstitutionId(String userId, String institutionId){
        int userIdInt = Integer.parseInt(userId);
        int institutionIdInt = Integer.parseInt(institutionId);
        UserInstitutionProfile userInstitutionProfile = userInstitutionProfileRepository.findByUserIdAndInstitutionId(userIdInt, institutionIdInt);
        if(userInstitutionProfile == null || !userInstitutionProfile.getStatus().equals(Constants.UserStatus.ACTIVE)){
            return null;
        }
        return authorityRepository.findAdminAuthorityByUserIdAndInstitutionId(userIdInt,institutionIdInt);
    }

}
