package org.viacode.library.exception;

/**
 * VIAcode
 * Created by IVolkov on 8/12/2014.
 */
public class EntityConflictException extends EntityException {

    public EntityConflictException() {
    }

    public EntityConflictException(String message) {
        super(message);
    }
}
