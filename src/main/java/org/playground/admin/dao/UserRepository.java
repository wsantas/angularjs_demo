package org.playground.admin.dao;


import org.playground.admin.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsantasiero on 8/13/14.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("select u FROM User u, Authority a where u.id=a.userId and a.authority=:authority and a.institutionId=:institutionId")
    List<User> findAllByAuthority(@Param("authority") String authority, @Param("institutionId") int institutionId, Sort sort);

    @Query("select distinct u FROM User u, Authority a where u.id=a.userId and a.institutionId=:institutionId")
    List<User> findAllByInstitutionId(Sort sort, @Param("institutionId") int institutionId);

    @Query("select distinct u FROM User u, Authority a where u.id=a.userId and a.institutionId=:institutionId and a.authority=:authority")
    List<User> findByInstitutionIdAndAuthority(Sort sort, @Param("institutionId") int institutionId, @Param("authority") String authority);

    @Query("select distinct u FROM User u, Authority a where u.id=a.userId and a.authority='Observer' and a.institutionId=:institutionId")
    List<User> findAllObserversByInstitutionId(Sort sort, @Param("institutionId") int institutionId);

    @Query("select distinct u FROM User u, Authority a where u.id=a.userId and a.authority='Instructor' and a.institutionId=:institutionId")
    List<User> findAllInstructorsByInstitutionId(Sort sort, @Param("institutionId") int institutionId);

    @Query("select distinct u FROM User u, Authority a where u.id=:userId and u.id=a.userId and a.institutionId=:institutionId")
    User findByUserIdAndInstitutionId(@Param("userId") int userId, @Param("institutionId") int institutionId);


    User findByUsername(String username);
    User findByEmail(String email);

    @Transactional(value="transactionManager", readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Modifying
    @Query("update User u SET u.password=:password WHERE u.firstName=:firstName and u.lastName=:lastName and u.email=:email")
    void updateUserPassword(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("email") String email, @Param("password") String password);

    @Transactional(value="transactionManager", readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Modifying
    @Query("update User u SET u.status=:status WHERE u.id=:id")
    void updateUserStatus(@Param("id") int id, @Param("status") String status);

    User findById(Integer id);

    List<User> findAll();
}
