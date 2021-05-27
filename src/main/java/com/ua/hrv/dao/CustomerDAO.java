package com.ua.hrv.dao;

import com.ua.hrv.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer> {
    Customer findByUsername(String username);

    Customer findByActivationCode(String code);


}
