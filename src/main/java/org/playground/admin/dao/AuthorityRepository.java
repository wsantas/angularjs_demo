package org.playground.admin.dao;

import org.playground.admin.model.Authority;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsantasiero on 9/9/14.
 */
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {

    @Transactional(value="transactionManager", readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Modifying
    @Query("delete from Authority a where a.userId=:userId and a.institutionId=:institutionId")
    void deleteByUserIdAndInstitutionId(@Param("userId") int userId, @Param("institutionId") int institutionId);

    @Query("select a from Authority a where a.authority <> 'USER' and a.userId=:userId")
    List<Authority> findByUserId(@Param("userId") int userId);

    @Query("select a from Authority a where a.authority = 'Admin' and a.userId=:userId and a.institutionId=:institutionId")
    List<Authority> findAdminAuthorityByUserIdAndInstitutionId(@Param("userId") int userId, @Param("institutionId") int institutionId);

    @Query("select a from Authority a where a.userId=:userId and a.institutionId=:institutionId")
    List<Authority> findAuthorityByUserIdAndInstitutionId(@Param("userId") int userId, @Param("institutionId") int institutionId);
}
