package com.ua.hrv.util;

import com.ua.hrv.models.Customer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Customer.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Customer customer = (Customer) o;
        if (customer.getPassword().length()<6){
            errors.rejectValue("password","message.length.error");
        }if (customer.getUsername().length()<4){
            errors.rejectValue("username","message.length.error1");
        }
    }
}
