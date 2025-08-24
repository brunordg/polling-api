package com.nps.pollingapi.repository;

import com.nps.pollingapi.entity.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, Long> {

    Optional<UserPrincipal> findFirstByEmail(String email);

}