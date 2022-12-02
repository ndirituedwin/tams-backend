package com.tamsbeauty.Exceptions;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String service_not_found) {
        super(service_not_found);
    }
}
