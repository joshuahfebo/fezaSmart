package com.fezaschools.fezasmart.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByUserRoleRolesId(Integer id);

    java.util.Optional<User> findByUsername(String username);

    java.util.Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

}
