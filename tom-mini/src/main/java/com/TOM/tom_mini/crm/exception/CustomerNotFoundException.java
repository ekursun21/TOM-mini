package com.TOM.tom_mini.crm.exception;



public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer with ID " + id + " could not be found.");
    }
}