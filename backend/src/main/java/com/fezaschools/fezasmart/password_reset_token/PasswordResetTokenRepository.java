package com.fezaschools.fezasmart.password_reset_token;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findFirstByUserId(Integer id);

}
