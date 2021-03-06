package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select case when count(u)> 0 then true else false end from User u where lower(u.login) like lower(?1)")
    boolean existsByLogin(String login);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("select u from User u where lower(u.login) = lower(?1) ")
    Optional<User> findAuditorByLogin(String login);

    @Query("select u from User u where lower(u.login) = lower(?1) ")
    User findByLogin(String login);

    @Query("select u from User u inner join u.roles r where r.roleName in :roles")
    Page<User> findUsersByRole(@Param("roles") List<UserRole> roles, Pageable pageable);

}
