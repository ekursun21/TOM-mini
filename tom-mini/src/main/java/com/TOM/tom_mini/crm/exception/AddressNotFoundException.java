package com.TOM.tom_mini.crm.exception;


public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long id) {
        super("Address with ID " + id + " could not be found.");
    }
}
