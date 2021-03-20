package com.lickhunter.web.exceptions;

public class ServiceException extends Exception {

    public ServiceException() {
        super();
    }

    public ServiceException(String msg, Exception e) {
        super(msg, e);
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
