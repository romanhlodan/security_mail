package com.ua.hrv.service;

import com.ua.hrv.dao.CustomerDAO;
import com.ua.hrv.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public void save(Customer customer) {
        customerDAO.save(customer);
    }

    @Override
    public boolean activateCustomer(String code) {
        Customer customer = customerDAO.findByActivationCode(code);
        if (customer == null) {
            return false;
        }
        customer.setEnabled(true);
        return true;
    }

    @Override
    public Optional<Customer> findById(int id) {
        return customerDAO.findById(id);
    }


    @Override
    public void flush(Customer customer) {
        customerDAO.flush();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerDAO.findByUsername(username);
    }

}
