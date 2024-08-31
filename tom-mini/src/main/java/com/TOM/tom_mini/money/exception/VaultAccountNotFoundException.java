package com.TOM.tom_mini.money.exception;

public class VaultAccountNotFoundException extends RuntimeException {
    public VaultAccountNotFoundException() {
        super("Bank's vault account not found.");
    }
}
