package com.lickhunter.web.utils;

import com.lickhunter.web.constants.ErrorCode;
import com.lickhunter.web.exceptions.ServiceException;

public class APIPreconditionsUtil {

    private APIPreconditionsUtil() {}

    /**
     * Check if some value was found, otherwise throw exception
     *
     * @param expression
     *            has value true if found, otherwise false
     * @throws ServiceException
     *            if expression is false, value is not found
     */
    public static void checkFound(final boolean expression) throws ServiceException {
        if (!expression) {
            throw new ServiceException(ErrorCode.RESOURCE_NOT_FOUND.getValue());
        }
    }

    /**
     * Check if some value was found, otherwise throw exception
     *
     * @param resource
     *            has value true if found, otherwise false
     * @throws ServiceException
     *            if expression is false, value is not found
     */
    public static <T> T checkFound(final T resource) throws ServiceException {
        if (resource == null) {
            throw new ServiceException(ErrorCode.RESOURCE_NOT_FOUND.getValue());
        }
        return resource;
    }
}
