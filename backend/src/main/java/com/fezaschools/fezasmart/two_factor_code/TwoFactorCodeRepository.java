package com.fezaschools.fezasmart.two_factor_code;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Integer> {

    TwoFactorCode findFirstByUserId(Integer id);

}
