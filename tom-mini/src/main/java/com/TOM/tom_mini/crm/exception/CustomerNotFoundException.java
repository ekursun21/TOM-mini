package com.TOM.tom_mini.crm.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID id) {
        super("Customer with ID " + id + " could not be found.");
    }
}