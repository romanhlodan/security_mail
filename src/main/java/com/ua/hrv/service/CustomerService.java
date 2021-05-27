package com.ua.hrv.service;

import com.ua.hrv.models.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface CustomerService extends UserDetailsService {

    void save(Customer customer);

    boolean activateCustomer(String code);

    Optional<Customer> findById(int id);

    void flush (Customer customer);
}
