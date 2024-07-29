package com.TOM.tom_mini.crm.exception;

import java.util.UUID;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(UUID id) {
        super("Address with ID " + id + " could not be found.");
    }
}
