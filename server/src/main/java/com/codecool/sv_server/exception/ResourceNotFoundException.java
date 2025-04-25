package com.codecool.sv_server.exception;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String missingRes) {
        super("Resource not found: " + missingRes, 404);
    }

    @Override
    public int getStatus() {
        return super.getStatus();
    }
}
