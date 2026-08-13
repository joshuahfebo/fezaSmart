package com.fezaschools.fezasmart.api_key;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

    ApiKey findFirstBySchoolId(Integer id);

}
