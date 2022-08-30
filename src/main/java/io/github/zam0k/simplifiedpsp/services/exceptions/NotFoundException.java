package io.github.zam0k.simplifiedpsp.services.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Object cannot be found");
    }
}
