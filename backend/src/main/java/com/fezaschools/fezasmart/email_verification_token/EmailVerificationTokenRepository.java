package com.fezaschools.fezasmart.email_verification_token;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Integer> {

    EmailVerificationToken findFirstByUserId(Integer id);

}
