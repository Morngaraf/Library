package org.viacode.library.exception;

/**
 * VIAcode
 * Created by IVolkov on 8/12/2014.
 */
public class InternalServerErrorException extends Exception {
    public InternalServerErrorException() {
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }
}
