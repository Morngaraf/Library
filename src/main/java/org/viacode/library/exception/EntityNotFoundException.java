package org.viacode.library.exception;

/**
 * VIAcode
 * Created by IVolkov on 8/12/2014.
 */
public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
