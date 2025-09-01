package com.zotriverse.demo.repository;

import com.zotriverse.demo.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);

    @Query("SELECT a FROM Account a WHERE a.id IN (" +
            " SELECT v.user.id FROM Viewhistory v " +
            " GROUP BY v.user.id " +
            " HAVING MAX(v.updateAt) < :lastActiveDate)")
    List<Account> findUsersInactiveSince(@Param("lastActiveDate") Date lastActiveDate);

}
